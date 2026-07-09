#!/usr/bin/env python3
"""이벤트 백본 라이브 대시보드 서버 — 루티프로 미들마일 흐름.

실제 구동 중인 예제(docker compose)의 각 서비스 DB(oms/tms/bms/orchestrator)를
psql로 폴링해 outbox/inbox/도메인/saga 스냅샷을 JSON으로 내려주고, 6개 시나리오
실행(오더 생성 + orchestrator 액션들)을 백그라운드 스레드로 대신 돌린다. 표준 라이브러리만.

실행: python3 monitor/serve.py   (docker compose 가 떠 있어야 함)
      브라우저 http://localhost:8900
"""
import json
import os
import re
import subprocess
import threading
import time
import urllib.error
import urllib.request
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlparse, parse_qs

HERE = os.path.dirname(os.path.abspath(__file__))
EXAMPLE_DIR = os.path.dirname(HERE)
PORT = int(os.environ.get("MONITOR_PORT", "8900"))
OMS = os.environ.get("OMS_URL", "http://localhost:8080")
TMS = os.environ.get("TMS_URL", "http://localhost:8081")
ORCH = os.environ.get("ORCH_URL", "http://localhost:8083")

# 각 서비스 DB에서 한 방 쿼리로 스냅샷(JSON) 조립. outbox 이벤트엔 payload의 orderId를 함께 실어
# 대시보드가 "오더 생애주기" 단위로 이벤트를 묶을 수 있게 한다(액션마다 correlationId는 달라지므로).
_OUTBOX = ("(select coalesce(json_agg(x),'[]') from (select event_id::text as event_id, "
           "event_type, aggregate_id, payload->>'orderId' as order_id, correlation_id, seq, "
           "(published_at is not null) as published, to_char(occurred_at,'HH24:MI:SS') as ts "
           "from outbox order by seq desc limit 120) x)")
_INBOX = ("(select coalesce(json_agg(x),'[]') from (select consumer_group, event_id::text as event_id, "
          "to_char(processed_at,'HH24:MI:SS') as ts from inbox order by processed_at desc limit 120) x)")

QUERIES = {
    "oms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select order_id as id, status, "
            "(origin||'→'||destination) as ref, to_char(updated_at,'HH24:MI:SS') as ts "
            "from orders order by updated_at desc limit 10) x))"),
    "tms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select dispatch_id as id, order_id as ref, status, "
            "to_char(updated_at,'HH24:MI:SS') as ts from dispatches order by updated_at desc limit 10) x))"),
    "bms": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
            ",'domain',(select coalesce(json_agg(x),'[]') from (select settlement_id as id, order_id as ref, status, "
            "to_char(updated_at,'HH24:MI:SS') as ts from settlements order by updated_at desc limit 10) x))"),
    "orchestrator": ("select json_build_object('outbox'," + _OUTBOX + ",'inbox'," + _INBOX +
                     ",'saga',(select coalesce(json_agg(x),'[]') from (select aggregate_id, status, current_step, "
                     "correlation_id, to_char(updated_at,'HH24:MI:SS') as ts "
                     "from saga_instance order by updated_at desc limit 40) x))"),
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


# ── 시나리오 실행 (백그라운드 스레드로 오더 생성 + orchestrator 액션 시퀀스) ──────────────
def _post(url):
    req = urllib.request.Request(url, method="POST")
    with urllib.request.urlopen(req, timeout=10) as r:
        return json.loads(r.read().decode())


def _create_order():
    return _post(OMS + "/demo/orders?amount=1250000")["orderId"]


def _saga_status(oid):
    try:
        req = urllib.request.Request(ORCH + "/demo/saga/" + oid, method="GET")
        with urllib.request.urlopen(req, timeout=5) as r:
            return (json.loads(r.read().decode()).get("saga") or {}).get("status")
    except Exception:  # noqa: BLE001
        return None


def _wait(oid, status, timeout=25):
    end = time.time() + timeout
    while time.time() < end:
        if _saga_status(oid) == status:
            return True
        time.sleep(0.4)
    return False


def _try(url):
    try:
        return _post(url)
    except Exception as e:  # noqa: BLE001
        return {"error": str(e)}


def _dispatch(oid):
    """배차 생성(TMS, 리소스=dispatch) → dispatchId 반환."""
    r = _try(TMS + "/demo/dispatches?orderId=" + oid)
    return (r or {}).get("dispatchId")


def _deliver(did):
    _try(TMS + "/demo/dispatches/" + did + "/deliver")


def _cancel_dispatch(did):
    _try(TMS + "/demo/dispatches/" + did + "/cancel")


def _cancel_order(oid):
    _try(OMS + "/demo/orders/" + oid + "/cancel")


# 시나리오 라벨 (실행 로직은 _run_sequence — 배차는 dispatchId를 받아 이후 조작에 사용).
SCENARIOS = {
    "s1": "정상 완주", "s2": "배차취소→미배차 복귀", "s3": "배차취소→재배차→완주",
    "s4": "미배차 오더 취소", "s5": "배차된 오더 취소 거부", "s6": "중복 이벤트 멱등",
}


def _reproduce(oid, event_type):
    """이 오더의 해당 이벤트 봉투를 토픽에서 찾아 그대로 재발행(중복 → inbox 멱등 시연)."""
    topic = ".".join(event_type.split(".")[:2])
    for m in _read_topic(topic):
        if m.get("eventType") == event_type and oid in m.get("full", ""):
            _produce(topic, m["full"])
            return


def _run_sequence(oid, scenario):
    if scenario == "s1":
        _wait(oid, "CREATED"); did = _dispatch(oid); _wait(oid, "DISPATCHED"); _deliver(did)
    elif scenario == "s2":
        _wait(oid, "CREATED"); did = _dispatch(oid); _wait(oid, "DISPATCHED"); _cancel_dispatch(did)
    elif scenario == "s3":
        _wait(oid, "CREATED"); did = _dispatch(oid); _wait(oid, "DISPATCHED"); _cancel_dispatch(did)
        _wait(oid, "CREATED"); did2 = _dispatch(oid); _wait(oid, "DISPATCHED"); _deliver(did2)
    elif scenario == "s4":
        _wait(oid, "CREATED"); _cancel_order(oid)
    elif scenario == "s5":
        _wait(oid, "CREATED"); _dispatch(oid); _wait(oid, "DISPATCHED"); _cancel_order(oid)
    elif scenario == "s6":
        _wait(oid, "CREATED"); _dispatch(oid); _wait(oid, "DISPATCHED"); _reproduce(oid, "tms.dispatch.created")


def run_scenario(scenario):
    if scenario not in SCENARIOS:
        return {"error": "unknown scenario: " + scenario}
    oid = _create_order()
    threading.Thread(target=_run_sequence, args=(oid, scenario), daemon=True).start()
    return {"orderId": oid, "scenario": scenario, "label": SCENARIOS[scenario]}


# ── 개별 HTTP 액션: 컨트롤러의 @PostMapping을 자동 파싱해 버튼을 만든다 ────────────────
CONTROLLERS = [
    ("oms", "oms-service/src/main/java/com/wemeet/eventbackbone/oms/api/OrderController.java"),
    ("tms", "tms-service/src/main/java/com/wemeet/eventbackbone/tms/api/TmsController.java"),
]


def scan_actions():
    """컨트롤러 소스의 @RequestMapping 접두 + @PostMapping을 파싱해 액션(엔드포인트) 목록을 만든다."""
    out = []
    for svc, rel in CONTROLLERS:
        try:
            with open(os.path.join(EXAMPLE_DIR, rel), encoding="utf-8") as f:
                src = f.read()
        except OSError:
            continue
        pm = re.search(r'@RequestMapping\("([^"]*)"\)', src)
        prefix = pm.group(1) if pm else ""
        for ch in src.split("@PostMapping")[1:]:
            p = re.match(r'\s*\(\s*"([^"]*)"\s*\)', ch)   # 경로엔 {var}가 있으니 시작부에서 따옴표째 추출
            if not p:
                continue
            path = prefix + p.group(1)
            mb = re.search(r'\)\s*\{', ch)                 # 메소드 바디 여는 ') {' 까지가 시그니처
            head = ch[:mb.start()] if mb else ch[:400]
            path_vars = re.findall(r'\{(\w+)\}', path)
            req_params = [m.group(2) for m in re.finditer(r'@RequestParam(\([^)]*\))?\s+\w+\s+(\w+)', head)
                          if "defaultValue" not in (m.group(1) or "")]
            out.append({"svc": svc, "method": "POST", "path": path,
                        "pathVars": path_vars, "reqParams": req_params})
    return out


def fire(svc, path):
    """대시보드가 채운 구체 경로를 실제 서비스(OMS/TMS)로 프록시 발사(브라우저 CORS 회피)."""
    base = OMS if svc == "oms" else TMS
    try:
        return _post(base + path)
    except urllib.error.HTTPError as e:
        try:
            body = json.loads(e.read().decode())
        except Exception:  # noqa: BLE001
            body = {}
        return {"status": e.code, "error": body.get("message") or ("HTTP " + str(e.code))}
    except Exception as e:  # noqa: BLE001
        return {"error": str(e)}


# ── DLT 운영 (§7.1.6): 독소 주입 · DLT 조회 · 재주입 ─────────────────────────
KAFKA_BIN = "/opt/kafka/bin"
DLT_TOPICS = ["oms.cmd.DLT", "bms.cmd.DLT",
              "oms.order.DLT", "tms.dispatch.DLT", "bms.settlement.DLT"]


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
            scenario = (parse_qs(p.query).get("scenario", ["s1"])[0])
            try:
                self._send(200, json.dumps(poison() if scenario == "poison" else run_scenario(scenario)))
            except Exception as e:  # noqa: BLE001
                self._send(502, json.dumps({"error": str(e)}))
        elif p.path == "/api/actions":
            self._send(200, json.dumps({"actions": scan_actions(), "bases": {"oms": OMS, "tms": TMS}}))
        elif p.path == "/api/fire":
            q = parse_qs(p.query)
            self._send(200, json.dumps(fire(q.get("svc", ["oms"])[0], q.get("path", [""])[0])))
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
