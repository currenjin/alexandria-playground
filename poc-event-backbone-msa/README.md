# event-backbone-example

확정된 **이벤트 백본**(`design/framework/공통-확정.md §7.1.1~7.1.8`)을 MSA로 구현한 실행 가능한 예제.
도메인은 **루티프로 미들마일 흐름**(오더 → 배차 → 배송 → 정산)을 본떴다.
**OMS·TMS·BMS는 각각 독립 배포되는 비즈 서비스로 자기 API·리소스를 소유**하고, **orchestrator는 플랫폼 오너 소유의 액션별 사가**(컨트롤러 없이 사실 이벤트를 소비해 커맨드로 코디네이션·보상)다. 이 예제의 핵심은 **2PC 없이 크로스서비스 정합성**을 지키는 것 — **오더 애그리거트(OMS)가 생애주기의 단일 권위**로 낙관적 잠금 위에서 전이를 직렬 판정하고, 각 액션은 **자기 사가**로 묶여 실패 시 **보상**한다. 모두 Kafka로만 통신하고, 각 서비스는 **레이어드 아키텍처**(api / application / domain / infrastructure).

> 🧑‍💻 **비즈니스 개발자라면** → [GUIDE.md](GUIDE.md) — 당신이 할 일(3단계)만 먼저, 그 아래로 내부 로직 뎁스다운.

## 모듈 구조

```
event-backbone-example/
├── contracts/             # 공유 계약: 이벤트·커맨드 record + @EventContract (Spring 무의존)
├── platform-core/         # 공통 인프라(라이브러리): envelope·EventPublisher·Outbox 릴레이·Inbox 멱등·DLT·사가 엔진(common/saga)
├── orchestrator-service/  # ★ 액션별 사가(플랫폼 오너 소유): DispatchSaga·CancelDispatchSaga·DeliverSaga. 무상태(사실→커맨드/보상). 컨트롤러 없음. DB=orchestrator (§7.1.7)
├── oms-service/           # 오더 = 생애주기 단일 권위. 자기 API(/orders): 생성·취소(규칙 판정). 전이는 가드+낙관적 잠금(@Version)으로 직렬 판정. DB=oms
├── tms-service/           # 배차(dispatch). 자기 API(/dispatches): 배차·배송완료·배차취소. 보상 커맨드(CancelDispatch)만 소비. DB=tms
└── bms-service/           # 정산. 사가의 CreateSettlement 커맨드 소비. DB=bms
```

- **오더 애그리거트 = 단일 권위(§7.1.7)**: 오더 상태를 바꾸는 건 오직 OMS다. 사가가 `DispatchOrder`·`DeliverOrder` 같은 **전이 시도 커맨드**를 보내면, OMS가 현재 status를 보고 **가드된 전이**를 낙관적 잠금(`@Version`) 위에서 판정 → 성공하면 사실(`OrderDispatched`…), 규칙 위반이면 거부 사실(`OrderDispatchRejected`·`OrderCancelRejected`)을 발행한다. 이래서 "배차확정과 오더취소가 동시에 와도" 락으로 직렬화되어 정합성이 깨지지 않는다.
- **액션별 사가**: 각 크로스서비스 액션(배차확정·배차취소·배송완주)이 **자기 사가 클래스**다. 무상태로 사실을 받아 다음 커맨드를 내고, 전이가 거부되면 앞 단계를 **보상**한다(예: 배차확정 사가는 오더전이 거부 시 `CancelDispatch`로 배차를 되돌림). 상관은 **orderId**로 붙는다(배차·배송·취소가 여러 HTTP로 나뉘어도 같은 오더에 수렴).
- **의존 방향**: 모든 서비스 → `platform-core` → `contracts`. **서비스끼리는 서로 의존하지 않는다.** 사가가 `DispatchCreated`를 참조해도 TMS 모듈이 아니라 공유 `contracts`에 의존.
- 각 서비스 = 독립 Spring Boot 앱 + **자기 DB**(DB-per-service) + 자기 outbox/inbox. 통신은 Kafka로만.
- 레이어드: `api`(REST·Kafka 인바운드 어댑터) → `application`(유스케이스·사가) → `domain`(엔티티·포트) → `infrastructure`(JDBC 어댑터).

## 실행 (MSA 한 방)

```bash
docker compose up --build      # Kafka + PostgreSQL(4 DB) + orchestrator·OMS·TMS·BMS
```

포트: **OMS :8080** · **TMS :8081** (orchestrator·BMS는 외부 HTTP 액션이 없다 — 사가/정산은 이벤트로만 동작).
로컬(도커 인프라만) 개발: `docker compose up kafka postgres` 후 각 서비스를 `./gradlew :oms-service:bootRun` 등으로.

## API 호출 케이스

각 서비스가 **자기 리소스 API**를 노출한다. 오더 상태는 액션 → 사실 이벤트 → 사가 커맨드 → **OMS 권위 전이**를 거쳐 **비동기로** 바뀌므로, 조회는 잠깐(1~2초) 뒤에 확인한다. 상태의 유일한 진실은 **OMS 오더 status**(`GET /demo/state`)다.

| 액션 | 요청 | 소유 |
| --- | --- | --- |
| 오더 생성 | `POST :8080/demo/orders` | OMS |
| 오더 취소 | `POST :8080/demo/orders/{orderId}/cancel` | OMS (규칙 판정) |
| 배차 | `POST :8081/demo/dispatches?orderId={orderId}` → `{dispatchId}` | TMS |
| 배송완료 | `POST :8081/demo/dispatches/{dispatchId}/deliver` | TMS |
| 배차취소 | `POST :8081/demo/dispatches/{dispatchId}/cancel` | TMS |
| 오더 상태 조회(권위) | `GET :8080/demo/state/{orderId}` | OMS |

### 1. 정상 완주 — 생성 → 배차 → 배송 → 정산

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
curl "http://localhost:8080/demo/state/$OID"         # order=CREATED
DID=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=DISPATCHED  (배차확정 사가가 오더를 전이)
curl -XPOST "http://localhost:8081/demo/dispatches/$DID/deliver"
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=SETTLED  (배송완주 사가: 배송→정산→정산완료)
```

### 2. 배차 취소 → 미배차 복귀

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
DID=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)
# order=DISPATCHED 된 것 확인 후
curl -XPOST "http://localhost:8081/demo/dispatches/$DID/cancel"
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=CREATED (배차취소 사가가 오더를 미배차로 되돌림)
```

### 3. 재배차 → 완주 (되돌린 뒤 다시)

```bash
# 2번(배차취소)에 이어, 미배차(CREATED)로 돌아온 오더를 다시 배차
DID2=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)   # 새 배차
curl -XPOST "http://localhost:8081/demo/dispatches/$DID2/deliver"
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=SETTLED ✅
```

### 4. 미배차 오더 즉시 취소

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
curl -XPOST "http://localhost:8080/demo/orders/$OID/cancel"      # 배차 전이라 취소 성립(로컬 TX)
curl "http://localhost:8080/demo/state/$OID"         # order=CANCELLED
```

### 5. 배차된 오더 취소 시도 → 거부 (핵심 규칙)

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
DID=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)
# order=DISPATCHED 확인 후 취소 시도
curl -XPOST "http://localhost:8080/demo/orders/$OID/cancel"
curl "http://localhost:8080/demo/state/$OID"         # order=DISPATCHED 유지 (취소 안 됨)
# OMS 애그리거트가 "배차된 오더는 취소 불가"를 자체 판정 → oms.order 토픽에 OrderCancelRejected 사실 발행
```

### 6. 동시성 — 배차확정 + 오더취소 동시 (정합성 핵심)

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
# 거의 동시에 배차확정과 오더취소를 쏜다 (경합)
curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" &
curl -s -XPOST "http://localhost:8080/demo/orders/$OID/cancel" & wait
sleep 4
curl "http://localhost:8080/demo/state/$OID"
# 결과는 둘 중 하나로 '반드시' 수렴한다(중간 garbage 없음):
#   (a) order=CANCELLED · 활성 배차 0   — 취소가 먼저 커밋 → 뒤늦은 DispatchOrder는 거부 → 배차확정 사가가 CancelDispatch로 보상
#   (b) order=DISPATCHED · 활성 배차 1  — 배차가 먼저 커밋 → 뒤늦은 취소는 규칙 위반으로 거부(5번과 동일)
# 오더 애그리거트의 낙관적 잠금이 두 전이를 직렬화하므로 "오더는 DISPATCHED인데 배차는 취소됨" 같은 모순이 생기지 않는다.
```

### 7. 게이트/멱등 (참고)

- **잘못된 순서** — 배송완료/배차취소를 활성 배차가 없을 때 호출하면 `409 CONFLICT`(TMS가 자기 상태로 거부).
- **취소된/정산된 오더 배차** — OMS가 기대 상태(CREATED)가 아니면 `DispatchOrder`를 거부(`OrderDispatchRejected`) → 배차확정 사가가 배차를 보상취소(orphan 배차 없음).
- **중복 이벤트** — 같은 `eventId`가 재전달돼도 소비 측 inbox(ON CONFLICT)가 1회만 처리(상태 재전이 없음).
- **순서 역전 self-heal** — 배송완료가 오더 DISPATCHED보다 먼저 도착하면, OMS가 무시하지 않고 **재시도**(§7.1.6 백오프)로 오더가 따라잡은 뒤 반영한다.

## 실시간 관찰 — 라이브 대시보드 (`monitor/`)

`docker compose up` 후, **실제 구동 중인 스택**에서 이벤트·프로세스·outbox·inbox·도메인이 흐르는 걸 브라우저로 실시간 관찰한다.

```bash
python3 monitor/serve.py       # 표준 라이브러리만 — 별도 설치 없음
# → http://localhost:8900
```

- 각 서비스 DB(oms/tms/bms/orchestrator)를 폴링해 **outbox(발행 여부)·inbox(멱등)·도메인 상태**를 **오더(orderId) 생애주기 단위**로 그린다. 생애주기 상태 배지의 권위는 **OMS 오더 status**(사가가 아니라 오더).
- **HTTP 액션 바** — 컨트롤러의 `@PostMapping`을 자동 발견해 엔드포인트별 버튼을 만든다. 오더를 클릭(focus)해 고른 뒤 개별 요청을 하나씩 쏜다(id는 그 오더 기준 자동 채움).
- **시나리오 버튼** — 위 1~6 흐름을 자동으로 순서 실행하는 빠른 데모.
- 각 흐름 스텝 앞에 🌐 **HTTP 진입 마커**(그 사실을 트리거한 액션)와, ◈ 이벤트 / ⌘ 커맨드 구분 표시.

> 시뮬레이션(위키)이 "코드 관통"을 짜인 데이터로 보여준다면, 이 대시보드는 **실제 DB를 읽어 실동작**을 보여준다. (둘 다 시연용 — 확정 설계 아님. 실운영 관측은 별도 APM.)

## MSA 이벤트 통신 흐름 (액션별 사가)

각 번호 흐름이 **하나의 사가**다. 사가는 사실을 받아 커맨드를 내고, **오더 애그리거트(OMS)가 커맨드를 권위로 판정**해 전이 성공 사실 또는 거부 사실을 되쏜다.

```
① 오더 등록 (로컬 TX, 사가 아님)
  POST /demo/orders → OMS.create → orders(CREATED) + oms.outbox (한 트랜잭션) → Kafka: oms.order / order.created

② 배차확정 사가 (DispatchSaga)
  POST /demo/dispatches → TMS: dispatches(DISPATCHED) 발행 → Kafka: tms.dispatch / dispatch.created
  [DispatchSaga] on dispatch.created → 커맨드 oms.cmd / dispatch_order
  [OMS] dispatch_order: CREATED면 → orders=DISPATCHED + oms.order / order.dispatched   (권위 전이)
                        아니면    → oms.order / order.dispatch_rejected                (거부)
  [DispatchSaga] on order.dispatch_rejected → 보상: tms.cmd / cancel_dispatch → 배차 되돌림

③ 배차취소 사가 (CancelDispatchSaga)
  POST /demo/dispatches/{id}/cancel → TMS: dispatches(CANCELLED) 발행 → tms.dispatch / dispatch.cancelled
  [CancelDispatchSaga] on dispatch.cancelled → 커맨드 oms.cmd / undispatch_order
  [OMS] undispatch_order: DISPATCHED면 → orders=CREATED + oms.order / order.undispatched (미배차 복귀)

④ 오더 취소 (로컬 TX, 사가 아님)
  POST /demo/orders/{id}/cancel → [OMS] CREATED면 orders=CANCELLED + order.cancelled / 배차 후면 불변 + order.cancel_rejected

⑤ 배송완주 사가 (DeliverSaga)
  POST /demo/dispatches/{id}/deliver → TMS: dispatches(DELIVERED) 발행 → tms.dispatch / dispatch.delivered
  [DeliverSaga] on dispatch.delivered → oms.cmd / deliver_order
  [OMS] deliver_order: DISPATCHED면 → orders=DELIVERED + oms.order / order.delivered (amount 실어줌)
  [DeliverSaga] on order.delivered → bms.cmd / create_settlement
  [BMS] on create_settlement → settlements(COMPLETED) 발행 → bms.settlement / settlement.completed
  [DeliverSaga] on settlement.completed → oms.cmd / settle_order
  [OMS] settle_order: DELIVERED면 → orders=SETTLED ✅
```

**사실 이벤트**(oms.order/tms.dispatch/bms.settlement)는 사가가 구독하고, **커맨드**(oms.cmd/tms.cmd/bms.cmd)는 각 서비스가 자기 것만 구독한다. 서비스는 자기가 어느 사가에 속하는지 모른다 — 사가가 사실을 엮어 다음 지시를 낼 뿐이다. **오더 상태를 바꾸는 유일한 주체는 OMS**이고, 사가는 "시도"만 한다.

## 코드 ↔ 확정 설계(§7.1.x)

| 절 | 개념 | 코드 |
| --- | --- | --- |
| §7.1.1 | envelope 8필드·논리명 eventType·**컨텍스트 자동주입**·**UUIDv7** | `common/event/Envelope · EventContract · OutboxEventPublisher · UuidV7 · common/context/FlowContext`(+`FlowContextFilter`: HTTP 진입점 자동·JWT 파싱 지점) |
| §7.1.2 | 토픽=애그리거트당(앞 두 마디)·**이름/파티션/리텐션 as-code·auto-create off** | `EventTypes.topicOf()` · `KafkaTopicConfig`+`event-topics.yml`(카탈로그) · compose `AUTO_CREATE_TOPICS_ENABLE=false` |
| §7.1.3 | Outbox 하이브리드·**활성 트랜잭션 밖 publish=예외**·발행 7일 보존 | `common/event/OutboxEventPublisher`(tx 검사) · `V1__outbox_inbox.sql` · `common/maintenance/RetentionCleaner` |
| §7.1.4 | 폴링 릴레이·at-least-once·**age of oldest message 감시**·전송 포트 | `common/outbox/OutboxRelay` · `common/event/transport/MessageTransport`(브로커 교체) · `RelayLagMonitor` |
| §7.1.5 | 수신 = **모듈이 자기 @KafkaListener 소유**·Inbox 멱등·같은 트랜잭션 | 각 서비스 `api/*EventListener`(설정 주입) · `common/event/EventConsumerSupport` + **`common/inbox/InboxRepository`**(ON CONFLICT) |
| §7.1.6 | 재시도→DLT·**지수 백오프 1s→4s→16s**·non-retryable 즉시 DLT | `common/event/config/EventInfraConfig`(DefaultErrorHandler + ExponentialBackOff + DeadLetterPublishingRecoverer) |
| §7.1.7 | **사가(코디네이션)/애그리거트(권위)/서비스 API(도메인)** 분리·orderId 상관·가드+낙관락·보상 | **사가**: `orchestrator-service: application/{DispatchSaga·CancelDispatchSaga·DeliverSaga}`(무상태) · **권위**: `oms-service: domain/Order`(@Version 낙관락 + 가드 전이) · **보상**: DispatchSaga→`CancelDispatch` · **상태형 사가 엔진(역량)**: `platform-core: common/saga/SagaStore·JdbcSagaStore`(이 예제 흐름은 무상태라 미사용) · 계약: `contracts` |
| §7.1.8 | docker-compose 한 방·골든패스 | `docker-compose.yml` · 아래 골든패스 |

## 골든패스 — 새 이벤트/핸들러 추가 (§7.1.8)

1. `contracts/`에 record + `@EventContract` + `ContractCatalog.ALL`에 등록
2. 발행: 서비스 application 레이어에서 `events.publish(new MyEvent(...))`
3. 소비: 핸들러 메소드에 `@EventHandler` 한 줄 (공통이 자동 등록 — 그룹=`platform.events.consumer-group`)
4. 흐름으로 엮을 땐 비즈 서비스는 자기 API·핸들러만 만들고, **액션을 사가로 조합(사실→커맨드, 실패 시 보상)하는 건 orchestrator에서**(플랫폼 오너). 오더 상태 전이는 **OMS 애그리거트의 가드**로만.

## 테스트 방법 (§7.1.8 3층)

- **단위** — `OmsServiceUnitTest` + `FakeEventPublisher`: 인프라 0, 비즈 로직만(취소 규칙 판정 포함).
- **계약** — `ContractCatalogTest`: 모든 `@EventContract` 유일성 + 토픽 유도(앞 두 마디) 규칙.
- **왕복(Testcontainers)** — `EventBackboneIT`: Postgres+Kafka 실제 기동으로 ① 발행(create→outbox→릴레이→Kafka) ② 소비+**Inbox 멱등**(oms.cmd `dispatch_order`→오더 DISPATCHED, 재전송해도 1회) ③ **DLT**(손상 메시지→`oms.cmd.DLT`) 검증.
- **크로스서비스 왕복** — `docker compose up --build` 후 위 "API 호출 케이스" 1~7 (특히 6번 동시성).

```bash
./gradlew :oms-service:test        # 3층 전부 (Testcontainers = Docker 필요)
./gradlew :oms-service:test --tests "*UnitTest" --tests "*ContractCatalogTest"   # 빠른 층만 (Docker 불필요)
```

## 예제라서 여전히 단순화한 것

- **사가 = 무상태 구체 코디네이터**(가독성): 실제는 step/flow 선언형 엔진(§7.1.7, Eventuate Tram 스타일 — 타임아웃·자동 재시도·상태 저장). 원칙(액션별 사가·orderId 상관·애그리거트 권위·가드+낙관락·보상·멱등·비즈 서비스 무지)은 동일. 상태형 사가 엔진(`SagaStore`)은 platform-core에 역량으로 존재하지만, 이 단순 흐름은 무상태 사가로 충분해 쓰지 않는다.
- **Schema Registry 없음**(JSON 직접) — eventType 계약은 `ContractCatalog` + 계약 테스트로 대체. 실제는 레지스트리 + CI 게이트.
- 4 DB를 한 PostgreSQL 인스턴스에(예제 편의). 실제 DB-per-service는 인스턴스 분리 가능.
- **범위 밖**(§7.1.x 문서 참조): CDC 릴레이 승격 · JWT 테넌트 검증 · BO 재주입 UI · 이벤트 스토어(콜드) · 오더 1→N 선적(FW식 상관키).
