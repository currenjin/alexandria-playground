#!/usr/bin/env python3
"""이벤트 백본 라이브 대시보드 서버.

실제 구동 중인 예제(docker compose)의 각 서비스 DB(oms/tms/bms/orchestrator)를
psql로 폴링해 outbox/inbox/도메인/saga 스냅샷을 JSON으로 내려주고, 시나리오
실행(POST /demo/orders)을 대신 호출한다. 표준 라이브러리만 사용(무의존).

실행: python3 monitor/serve.py   (docker compose 가 떠 있어야 함)
      브라우저 http://localhost:8900
"""
import json
import os
import subprocess
import urllib.request
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlparse, parse_qs

HERE = os.path.dirname(os.path.abspath(__file__))
EXAMPLE_DIR = os.path.dirname(HERE)
PORT = int(os.environ.get("MONITOR_PORT", "8900"))
OMS = os.environ.get("OMS_URL", "http://localhost:8080")

# 각 서비스 DB에서 한 방 쿼리로 스냅샷(JSON) 조립. 릴레이 동작값=컬럼이라 그대로 읽는다.
_OUTBOX = ("(select coalesce(json_agg(x),'[]') from (select event_id::text as event_id, "
           "event_type, aggregate_id, correlation_id, seq, (published_at is not null) as published, "
           "to_char(occurred_at,'HH24:MI:SS') as ts from outbox order by seq desc limit 60) x)")
_INBOX = ("(select coalesce(json_agg(x),'[]') from (select consumer_group, event_id::text as event_id, "
          "to_char(processed_at,'HH24:MI:SS') as ts from inbox order by processed_at desc limit 60) x)")

QUERIES = {
    "oms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select order_id as id, status, "
            "to_char(updated_at,'HH24:MI:SS') as ts from orders order by updated_at desc limit 8) x))"),
    "tms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select trip_id as id, order_id as ref, status, "
            "to_char(updated_at,'HH24:MI:SS') as ts from trips order by updated_at desc limit 8) x))"),
    "bms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select settlement_id as id, trip_id as ref, status, "
            "to_char(updated_at,'HH24:MI:SS') as ts from settlements order by updated_at desc limit 8) x))"),
    "orchestrator": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
                     ",'saga',(select coalesce(json_agg(x),'[]') from (select aggregate_id, status, current_step, "
                     "correlation_id, to_char(updated_at,'HH24:MI:SS') as ts from saga_instance order by updated_at desc limit 40) x))"),
}


def psql(db, q):
    try:
        out = subprocess.run(
            ["docker", "compose", "exec", "-T", "postgres", "psql", "-U", "ebe", "-d", db, "-tAc", q],
            cwd=EXAMPLE_DIR, capture_output=True, text=True, timeout=15)
        s = out.stdout.strip()
        if not s:
            return {"_error": (out.stderr.strip() or "empty")}
        return json.loads(s)
    except Exception as e:  # noqa: BLE001
        return {"_error": str(e)}


def snapshot():
    return {"services": {db: psql(db, q) for db, q in QUERIES.items()}}


def run_scenario(scenario):
    amount = "0" if scenario == "comp" else "1250000"
    req = urllib.request.Request(OMS + "/demo/orders?amount=" + amount, method="POST")
    with urllib.request.urlopen(req, timeout=10) as r:
        return json.loads(r.read().decode())


# ── DLT 운영 (§7.1.6): 독소 주입 · DLT 조회 · 재주입 ─────────────────────────
KAFKA_BIN = "/opt/kafka/bin"
DLT_TOPICS = ["oms.cmd.DLT", "tms.cmd.DLT", "bms.cmd.DLT",
              "oms.order.DLT", "tms.trip.DLT", "bms.settlement.DLT"]


def _kafka(args, input_text=None, timeout=20):
    return subprocess.run(["docker", "compose", "exec", "-T", "kafka"] + args,
                          cwd=EXAMPLE_DIR, capture_output=True, text=True,
                          input=input_text, timeout=timeout)


def _produce(topic, value):
    _kafka([KAFKA_BIN + "/kafka-console-producer.sh", "--topic", topic,
            "--bootstrap-server", "localhost:9092"], input_text=value + "\n", timeout=15)


def poison():
    """손상(역직렬화 불가) 메시지를 oms.cmd에 넣는다 → 재시도 소진 → oms.cmd.DLT."""
    _produce("oms.cmd", "not-a-valid-envelope")
    return {"hint": "독소 메시지 주입", "orderId": "oms.cmd(손상)", "note": "역직렬화 실패 → oms.cmd.DLT"}


def _read_topic(topic, timeout_ms=7000, maxm=200):
    r = _kafka([KAFKA_BIN + "/kafka-console-consumer.sh", "--topic", topic, "--from-beginning",
                "--timeout-ms", str(timeout_ms), "--max-messages", str(maxm),
                "--bootstrap-server", "localhost:9092"], timeout=timeout_ms / 1000 + 10)
    out = []
    for line in r.stdout.splitlines():
        line = line.strip()
        if not line:
            continue
        item = {"full": line}
        try:
            j = json.loads(line)
            item["eventType"] = j.get("eventType")
            item["aggregateId"] = j.get("aggregateId")
            item["eventId"] = (j.get("eventId") or "")[:8]
        except Exception:  # noqa: BLE001
            item["eventType"] = "(손상 — 파싱 불가)"
            item["aggregateId"] = line[:24]
            item["eventId"] = ""
        out.append(item)
    return out


def read_dlt():
    import concurrent.futures
    result = []
    with concurrent.futures.ThreadPoolExecutor(max_workers=6) as ex:
        futs = {ex.submit(_read_topic, t): t for t in DLT_TOPICS}
        for fut in concurrent.futures.as_completed(futs):
            t = futs[fut]
            try:
                msgs = fut.result()
            except Exception:  # noqa: BLE001
                msgs = []
            if msgs:
                result.append({"topic": t, "count": len(msgs),
                               "messages": [{k: v for k, v in m.items() if k != "full"} for m in msgs]})
    result.sort(key=lambda x: x["topic"])
    return {"dlt": result}


def redrive(dlt_topic):
    """DLT 메시지를 원본 토픽으로 재발행. 같은 eventId라 소비 측 inbox 멱등으로 안전."""
    if not dlt_topic.endswith(".DLT"):
        return {"error": "not a DLT topic"}
    origin = dlt_topic[:-len(".DLT")]
    msgs = _read_topic(dlt_topic)
    for m in msgs:
        _produce(origin, m["full"])
    return {"redriven": len(msgs), "from": dlt_topic, "to": origin}


class Handler(BaseHTTPRequestHandler):
    def _send(self, code, body, ctype="application/json"):
        data = body if isinstance(body, bytes) else body.encode("utf-8")
        self.send_response(code)
        self.send_header("Content-Type", ctype + "; charset=utf-8")
        self.send_header("Content-Length", str(len(data)))
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        self.wfile.write(data)

    def do_GET(self):
        p = urlparse(self.path)
        if p.path in ("/", "/index.html"):
            try:
                with open(os.path.join(HERE, "dashboard.html"), "rb") as f:
                    self._send(200, f.read(), "text/html")
            except OSError as e:
                self._send(500, json.dumps({"error": str(e)}))
        elif p.path == "/api/state":
            self._send(200, json.dumps(snapshot()))
        elif p.path == "/api/run":
            scenario = (parse_qs(p.query).get("scenario", ["normal"])[0])
            try:
                self._send(200, json.dumps(poison() if scenario == "poison" else run_scenario(scenario)))
            except Exception as e:  # noqa: BLE001
                self._send(502, json.dumps({"error": str(e)}))
        elif p.path == "/api/dlt":
            self._send(200, json.dumps(read_dlt()))
        elif p.path == "/api/dlt/redrive":
            topic = parse_qs(p.query).get("topic", [""])[0]
            try:
                self._send(200, json.dumps(redrive(topic)))
            except Exception as e:  # noqa: BLE001
                self._send(502, json.dumps({"error": str(e)}))
        else:
            self._send(404, json.dumps({"error": "not found"}))

    def log_message(self, *args):  # 조용히
        pass


if __name__ == "__main__":
    print("이벤트 백본 라이브 대시보드 → http://localhost:%d  (example: %s)" % (PORT, EXAMPLE_DIR))
    ThreadingHTTPServer(("0.0.0.0", PORT), Handler).serve_forever()
