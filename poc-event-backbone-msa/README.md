# event-backbone-example

확정된 **이벤트 백본**(`design/framework/공통-확정.md §7.1.1~7.1.8`)을 MSA로 구현한 실행 가능한 예제.
도메인은 **루티프로 미들마일 흐름**(오더 → 배차 → 배송 → 정산)을 본떴다.
**OMS·TMS·BMS는 각각 독립 배포되는 비즈 서비스로 자기 API·리소스를 소유**하고, **orchestrator는 플랫폼 오너 소유의 중앙 프로세스 매니저**(컨트롤러 없이 이벤트를 소비해 커맨드로 코디네이션)다. 모두 Kafka로만 통신하고, 각 서비스는 **레이어드 아키텍처**(api / application / domain / infrastructure).

> 🧑‍💻 **비즈니스 개발자라면** → [GUIDE.md](GUIDE.md) — 당신이 할 일(3단계)만 먼저, 그 아래로 내부 로직 뎁스다운.

## 모듈 구조

```
event-backbone-example/
├── contracts/             # 공유 계약: 이벤트·커맨드 record + @EventContract (Spring 무의존)
├── platform-core/         # 공통 인프라(라이브러리): envelope·EventPublisher·Outbox 릴레이·Inbox 멱등·DLT·사가 엔진(common/saga)
├── orchestrator-service/  # ★ 중앙 프로세스 매니저(플랫폼 오너 소유): 흐름 정의 + saga_instance DB. 컨트롤러 없음. DB=orchestrator (§7.1.7)
├── oms-service/           # 오더. 자기 API(/orders): 생성·취소(규칙 판정). 상태 전이는 orchestrator 커맨드로. DB=oms
├── tms-service/           # 배차(dispatch). 자기 API(/dispatches): 배차·배송완료·배차취소. 사실만 발행(커맨드 소비 없음=publish-only). DB=tms
└── bms-service/           # 정산. orchestrator의 CreateSettlement 커맨드 소비. DB=bms
```

- **서비스 자율성(§7.1.7)**: 각 서비스는 **자기 리소스 API**로 행위하고 **사실 이벤트**를 발행한다. **orchestrator**(프로세스 매니저)가 그 사실을 소비해 오더 상태 동기화 커맨드(`Mark*`)·정산 트리거(`CreateSettlement`)를 발행하며, 프로세스를 **orderId(업무 키)**로 추적한다(요청별 correlationId가 아니라 — 배차·배송·취소가 여러 HTTP로 나뉘어도 같은 프로세스에 붙는다).
- **의존 방향**: 모든 서비스 → `platform-core` → `contracts`. **서비스끼리는 서로 의존하지 않는다.** orchestrator가 `DispatchCreated`를 참조해도 TMS 모듈이 아니라 공유 `contracts`에 의존.
- 각 서비스 = 독립 Spring Boot 앱 + **자기 DB**(DB-per-service) + 자기 outbox/inbox. 통신은 Kafka로만.
- 레이어드: `api`(REST·Kafka 인바운드 어댑터) → `application`(유스케이스·프로세스 매니저) → `domain`(엔티티·포트) → `infrastructure`(JDBC 어댑터).

## 실행 (MSA 한 방)

```bash
docker compose up --build      # Kafka + PostgreSQL(4 DB) + orchestrator·OMS·TMS·BMS
```

포트: **OMS :8080** · **TMS :8081** · **orchestrator :8083** (BMS는 외부 액션이 없어 포트 미개방).
로컬(도커 인프라만) 개발: `docker compose up kafka postgres` 후 각 서비스를 `./gradlew :oms-service:bootRun` 등으로.

## API 호출 케이스

각 서비스가 **자기 리소스 API**를 노출한다. 오더 상태는 액션 → 사실 이벤트 → orchestrator 커맨드를 거쳐 **비동기로** 바뀌므로, 조회는 잠깐(1~2초) 뒤에 확인한다.

| 액션 | 요청 | 소유 |
| --- | --- | --- |
| 오더 생성 | `POST :8080/demo/orders` | OMS |
| 오더 취소 | `POST :8080/demo/orders/{orderId}/cancel` | OMS (규칙 판정) |
| 배차 | `POST :8081/demo/dispatches?orderId={orderId}` → `{dispatchId}` | TMS |
| 배송완료 | `POST :8081/demo/dispatches/{dispatchId}/deliver` | TMS |
| 배차취소 | `POST :8081/demo/dispatches/{dispatchId}/cancel` | TMS |
| 오더 상태 조회 | `GET :8080/demo/state/{orderId}` | OMS |
| 프로세스 조회 | `GET :8083/demo/saga/{orderId}` | orchestrator |

### 1. 정상 완주 — 생성 → 배차 → 배송 → 정산

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
curl "http://localhost:8083/demo/saga/$OID"          # saga=CREATED
DID=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=DISPATCHED
curl -XPOST "http://localhost:8081/demo/dispatches/$DID/deliver"
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=SETTLED  (배송완료가 정산을 트리거)
curl "http://localhost:8083/demo/saga/$OID"          # saga=SETTLED ✅
```

### 2. 배차 취소 → 미배차 복귀

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
DID=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)
# order=DISPATCHED 된 것 확인 후
curl -XPOST "http://localhost:8081/demo/dispatches/$DID/cancel"
curl "http://localhost:8080/demo/state/$OID"         # (곧) order=CREATED (미배차 복귀)
curl "http://localhost:8083/demo/saga/$OID"          # saga=CREATED
```

### 3. 재배차 → 완주 (되돌린 뒤 다시)

```bash
# 2번(배차취소)에 이어, 미배차(CREATED)로 돌아온 오더를 다시 배차
DID2=$(curl -s -XPOST "http://localhost:8081/demo/dispatches?orderId=$OID" | jq -r .dispatchId)   # 새 배차
curl -XPOST "http://localhost:8081/demo/dispatches/$DID2/deliver"
curl "http://localhost:8083/demo/saga/$OID"          # saga=SETTLED ✅
```

### 4. 미배차 오더 즉시 취소

```bash
OID=$(curl -s -XPOST "http://localhost:8080/demo/orders?amount=1250000" | jq -r .orderId)
curl -XPOST "http://localhost:8080/demo/orders/$OID/cancel"      # 배차 전이라 취소 성립
curl "http://localhost:8080/demo/state/$OID"         # order=CANCELLED
curl "http://localhost:8083/demo/saga/$OID"          # saga=CANCELLED
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

### 6. 게이트/멱등 (참고)

- **잘못된 순서** — 배송완료/배차취소를 활성 배차가 없을 때 호출하면 `409 CONFLICT`(TMS가 자기 상태로 거부).
- **취소된/정산된 오더 배차** — orchestrator 프로세스가 기대 상태(CREATED)가 아니면 `DispatchCreated`를 무시(orphan 배차, 오더 불변).
- **중복 이벤트** — 같은 `eventId`가 재전달돼도 소비 측 inbox(ON CONFLICT)가 1회만 처리(상태 재전이 없음).
- **순서 역전 self-heal** — 오더 생성 직후 프로세스 시작(CREATED) 전에 배차하면, orchestrator가 무시하지 않고 **재시도**(§7.1.6 백오프)로 프로세스 준비 후 반영한다.

## 실시간 관찰 — 라이브 대시보드 (`monitor/`)

`docker compose up` 후, **실제 구동 중인 스택**에서 이벤트·프로세스·outbox·inbox·도메인이 흐르는 걸 브라우저로 실시간 관찰한다.

```bash
python3 monitor/serve.py       # 표준 라이브러리만 — 별도 설치 없음
# → http://localhost:8900
```

- 각 서비스 DB(oms/tms/bms/orchestrator)를 폴링해 **outbox(발행 여부)·inbox(멱등)·도메인 상태·saga_instance**를 **오더(orderId) 생애주기 단위**로 그린다.
- **HTTP 액션 바** — 컨트롤러의 `@PostMapping`을 자동 발견해 엔드포인트별 버튼을 만든다. 오더를 클릭(focus)해 고른 뒤 개별 요청을 하나씩 쏜다(id는 그 오더 기준 자동 채움).
- **시나리오 버튼** — 위 1~6 흐름을 자동으로 순서 실행하는 빠른 데모.
- 각 흐름 스텝 앞에 🌐 **HTTP 진입 마커**(그 사실을 트리거한 액션)와, ◈ 이벤트 / ⌘ 커맨드 구분 표시.

> 시뮬레이션(위키)이 "코드 관통"을 짜인 데이터로 보여준다면, 이 대시보드는 **실제 DB를 읽어 실동작**을 보여준다. (둘 다 시연용 — 확정 설계 아님. 실운영 관측은 별도 APM.)

## MSA 이벤트 통신 흐름 (정상 완주)

```
POST /demo/orders (OMS)
  OMS.create → orders(CREATED) + oms.outbox INSERT (한 트랜잭션) → 릴레이 → Kafka: oms.order / oms.order.created
  [orchestrator] on order.created → saga_instance CREATED (orderId 키)

POST /demo/dispatches?orderId (TMS)
  TMS.dispatch → dispatches(DISPATCHED) + 발행 → Kafka: tms.dispatch / dispatch.created
  [orchestrator] on dispatch.created → publish → Kafka: oms.cmd / mark_dispatched
  [OMS] on mark_dispatched → orders=DISPATCHED

POST /demo/dispatches/{id}/deliver (TMS)
  TMS.deliver → dispatches(DELIVERED) + 발행 → Kafka: tms.dispatch / dispatch.delivered
  [orchestrator] on dispatch.delivered → publish → oms.cmd / mark_delivered  +  bms.cmd / create_settlement
  [BMS] on create_settlement → settlements(COMPLETED) + 발행 → Kafka: bms.settlement / settlement.completed
  [orchestrator] on settlement.completed → publish oms.cmd / mark_settled → saga SETTLED ✅
  [OMS] on mark_settled → orders=SETTLED

취소: [TMS] dispatch.cancelled → [orchestrator] mark_undispatched → [OMS] orders=CREATED(미배차 복귀)
규칙: [OMS] cancel(배차 후) → orders 불변 + oms.order / cancel_rejected
```

**사실 이벤트**(oms.order/tms.dispatch/bms.settlement)는 orchestrator가 구독하고, **커맨드**(oms.cmd/bms.cmd)는 각 참여자 서비스가 구독한다. TMS는 커맨드를 구독하지 않는다(publish-only). 참여자는 자기가 어느 프로세스에 속하는지 모른다.

## 코드 ↔ 확정 설계(§7.1.x)

| 절 | 개념 | 코드 |
| --- | --- | --- |
| §7.1.1 | envelope 8필드·논리명 eventType·**컨텍스트 자동주입**·**UUIDv7** | `common/event/Envelope · EventContract · OutboxEventPublisher · UuidV7 · common/context/FlowContext`(+`FlowContextFilter`: HTTP 진입점 자동·JWT 파싱 지점) |
| §7.1.2 | 토픽=애그리거트당(앞 두 마디)·**이름/파티션/리텐션 as-code·auto-create off** | `EventTypes.topicOf()` · `KafkaTopicConfig`+`event-topics.yml`(카탈로그) · compose `AUTO_CREATE_TOPICS_ENABLE=false` |
| §7.1.3 | Outbox 하이브리드·**활성 트랜잭션 밖 publish=예외**·발행 7일 보존 | `common/event/OutboxEventPublisher`(tx 검사) · `V1__outbox_inbox.sql` · `common/maintenance/RetentionCleaner` |
| §7.1.4 | 폴링 릴레이·at-least-once·**age of oldest message 감시**·전송 포트 | `common/outbox/OutboxRelay` · `common/event/transport/MessageTransport`(브로커 교체) · `RelayLagMonitor` |
| §7.1.5 | 수신 = **모듈이 자기 @KafkaListener 소유**·Inbox 멱등·같은 트랜잭션 | 각 서비스 `api/*EventListener`(설정 주입) · `common/event/EventConsumerSupport` + **`common/inbox/InboxRepository`**(ON CONFLICT) |
| §7.1.6 | 재시도→DLT·**지수 백오프 1s→4s→16s**·non-retryable 즉시 DLT | `common/event/config/EventInfraConfig`(DefaultErrorHandler + ExponentialBackOff + DeadLetterPublishingRecoverer) |
| §7.1.7 | **프로세스 매니저(중앙)/서비스 API(도메인)** 분리·orderId 키·상태 동기화 커맨드·가드 | **flow**: `orchestrator-service: application/OrderFulfillmentSaga` · **엔진**: `platform-core: common/saga/SagaStore·JdbcSagaStore`(aggregateId 키) · **행위**: 각 서비스 API·커맨드 핸들러 · 계약: `contracts` |
| §7.1.8 | docker-compose 한 방·골든패스 | `docker-compose.yml` · 아래 골든패스 |

## 골든패스 — 새 이벤트/핸들러 추가 (§7.1.8)

1. `contracts/`에 record + `@EventContract` + `ContractCatalog.ALL`에 등록
2. 발행: 서비스 application 레이어에서 `events.publish(new MyEvent(...))`
3. 소비: 핸들러 메소드에 `@EventHandler` 한 줄 (공통이 자동 등록 — 그룹=`platform.events.consumer-group`)
4. 흐름으로 엮을 땐 비즈 서비스는 자기 API·핸들러만 만들고, **중앙 flow 조합(이벤트→커맨드)은 orchestrator에서**(플랫폼 오너).

## 테스트 방법 (§7.1.8 3층)

- **단위** — `OmsServiceUnitTest` + `FakeEventPublisher`: 인프라 0, 비즈 로직만(취소 규칙 판정 포함).
- **계약** — `ContractCatalogTest`: 모든 `@EventContract` 유일성 + 토픽 유도(앞 두 마디) 규칙.
- **왕복(Testcontainers)** — `EventBackboneIT`: Postgres+Kafka 실제 기동으로 ① 발행(create→outbox→릴레이→Kafka) ② 소비+**Inbox 멱등**(oms.cmd `mark_dispatched`→오더 DISPATCHED, 재전송해도 1회) ③ **DLT**(손상 메시지→`oms.cmd.DLT`) 검증.
- **크로스서비스 왕복** — `docker compose up --build` 후 위 "API 호출 케이스" 1~5.

```bash
./gradlew :oms-service:test        # 3층 전부 (Testcontainers = Docker 필요)
./gradlew :oms-service:test --tests "*UnitTest" --tests "*ContractCatalogTest"   # 빠른 층만 (Docker 불필요)
```

## 예제라서 여전히 단순화한 것

- **프로세스 매니저 = 얇은 구체 오케스트레이터**(가독성): 실제는 step/flow 선언형 엔진(§7.1.7, Eventuate Tram 스타일). 원칙(중앙 flow·orderId 매칭·상태 가드·멱등·비즈 서비스 무지)은 동일. 이 사용자 주도 흐름엔 자동 보상/타임아웃 대신 **취소(배차취소→미배차 복귀)** 로 되돌린다(사가 보상 역량 자체는 platform-core에 존재).
- **Schema Registry 없음**(JSON 직접) — eventType 계약은 `ContractCatalog` + 계약 테스트로 대체. 실제는 레지스트리 + CI 게이트.
- 4 DB를 한 PostgreSQL 인스턴스에(예제 편의). 실제 DB-per-service는 인스턴스 분리 가능.
- **범위 밖**(§7.1.x 문서 참조): CDC 릴레이 승격 · JWT 테넌트 검증 · BO 재주입 UI · 이벤트 스토어(콜드) · 오더 1→N 선적(FW식 상관키).
