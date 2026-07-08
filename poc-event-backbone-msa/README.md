# poc-event-backbone-msa

이벤트 기반 MSA의 **이벤트 백본**(봉투 · Outbox · 폴링 릴레이 · Inbox 멱등 · DLT · Saga)을 실제로 구현·검증한 예제.
**OMS·TMS·BMS는 각각 독립 배포되는 비즈 서비스**, **orchestrator는 플랫폼 오너 소유의 중앙 사가**다. 모두 Kafka 이벤트로만 통신하고, 각 서비스는 **레이어드 아키텍처**(api / application / domain / infrastructure).

> 본문에 나오는 절 번호(§7.1.x)는 이 예제의 설계 근거가 된 내부 설계 문서의 "이벤트 백본" 챕터를 가리키는 라벨이다.

## 모듈 구조

```
poc-event-backbone-msa/
├── contracts/             # 공유 계약: 이벤트·커맨드 record + @EventContract (Spring 무의존)
├── platform-core/         # 공통 인프라(라이브러리): 봉투·EventPublisher·Outbox 릴레이·Inbox 멱등·DLT·사가 엔진(common/saga)
├── orchestrator-service/  # ★ 중앙 사가(플랫폼 오너 소유): 흐름 정의 + saga_instance DB. DB=orchestrator (§7.1.7)
├── oms-service/           # 주문. 사가 step만(주문확정=진입, 취소=보상) — 사가를 모른다. REST 진입점. DB=oms
├── tms-service/           # 배차. 사가 step만. DB=tms
└── bms-service/           # 정산. 사가 step만. DB=bms
```

- **소유 모델(§7.1.7)**: **step = 도메인(비즈 서비스)** 소유(커맨드 핸들러), **flow = 중앙(플랫폼 오너)** 소유. 비즈 개발자는 Saga를 모른 채 자기 step만 구현하고, 사가 흐름은 `orchestrator-service`가 조합한다. 재사용 사가 엔진(`SagaStore`)은 `platform-core/common/saga`.
- **의존 방향**: 모든 서비스 → `platform-core` → `contracts`. **서비스끼리는 서로 의존하지 않는다.** 사가가 `TripDispatched`를 참조해도 TMS 모듈이 아니라 공유 `contracts`에 의존.
- 각 서비스 = 독립 Spring Boot 앱 + **자기 DB**(DB-per-service) + 자기 outbox/inbox. 통신은 Kafka로만.
- 레이어드: `api`(REST·Kafka 인바운드 어댑터) → `application`(유스케이스·사가 flow) → `domain`(엔티티·포트) → `infrastructure`(JDBC 어댑터).

## 실행 (MSA 한 방)

```bash
docker compose up --build      # Kafka + PostgreSQL(4 DB) + orchestrator·OMS·TMS·BMS 4개 서비스

# 정상 흐름 — 주문→배차→정산 (서비스 간 Kafka 이벤트로)
curl -XPOST "http://localhost:8080/demo/orders?amount=1250000"   # → {orderId, correlationId}
curl "http://localhost:8080/demo/state/ORD-xxxx"                 # OMS: order=CONFIRMED
curl "http://localhost:8083/demo/saga/ORD-xxxx"                  # orchestrator: saga=COMPLETED

# 보상 흐름 — 배차 불가(amount=0) → 중앙 사가가 주문 취소
curl -XPOST "http://localhost:8080/demo/orders?amount=0"
curl "http://localhost:8080/demo/state/ORD-yyyy"                 # OMS: order=CANCELLED
curl "http://localhost:8083/demo/saga/ORD-yyyy"                  # orchestrator: saga=COMPENSATED
```

주문 상태(OMS)와 사가 상태(orchestrator)를 **다른 서비스·다른 DB**에서 조회하는 점이 곧 MSA — 각 서비스는 자기 DB만 안다.

로컬(도커 인프라만) 개발: `docker compose up kafka postgres` 후 각 서비스를 `./gradlew :orchestrator-service:bootRun` 등으로.

## MSA 이벤트 통신 흐름

```
POST /demo/orders (OMS)
  OMS.confirm → orders + oms.outbox INSERT (한 트랜잭션) → 릴레이 → Kafka: oms.order / oms.order.confirmed
  [saga@orchestrator] on order.confirmed → saga_instance STARTED → publish → Kafka: tms.cmd / create_trip
  [TMS] on create_trip → trips DISPATCHED → publish → Kafka: tms.trip / dispatched
  [saga@orchestrator] on trip.dispatched → publish → Kafka: bms.cmd / schedule_settlement
  [BMS] on schedule_settlement → settlements SCHEDULED → publish → Kafka: bms.settlement / scheduled
  [saga@orchestrator] on settlement.scheduled → COMPLETED ✅

보상(amount=0): [TMS] create_trip → trip.creation_failed
  → [saga@orchestrator] CancelOrder → Kafka: oms.cmd → [OMS] orders CANCELLED → COMPENSATED
```

흐름 신호(oms.order/tms.trip/bms.settlement)는 **중앙 orchestrator**가 구독하고, 커맨드(oms.cmd/tms.cmd/bms.cmd)는 각 참여자 서비스가 구독한다. 참여자는 자기가 어느 사가에 속하는지 모른다.

## 코드 ↔ 설계(§7.1.x)

| 절 | 개념 | 코드 |
| --- | --- | --- |
| §7.1.1 | 봉투 8필드·논리명 eventType·컨텍스트 자동 | `platform-core: common/event/Envelope · EventContract · OutboxEventPublisher · common/context/FlowContext` |
| §7.1.2 | 토픽=애그리거트당(앞 두 마디) | `EventTypes.topicOf()` |
| §7.1.3 | Outbox 하이브리드·트랜잭션 밖 publish=예외 | `platform-core: common/event/OutboxEventPublisher` · `V1__outbox_inbox.sql` |
| §7.1.4 | 폴링 릴레이·at-least-once | `platform-core: common/outbox/OutboxRelay` (각 서비스가 자기 outbox 릴레이) |
| §7.1.5 | 컨슈머 그룹·Inbox 멱등·같은 트랜잭션 | `common/event/EventConsumerSupport` + **`common/inbox/InboxRepository`**(ON CONFLICT 원자 INSERT) · 각 서비스 `*EventListener`(그룹별) |
| §7.1.6 | 재시도→DLT(컨슈머가 produce) | `common/event/EventInfraConfig`(DefaultErrorHandler + DeadLetterPublishingRecoverer) |
| §7.1.7 | 사가 **step(도메인)/flow(중앙)** 분리·보상·타임아웃·계약 의존 한 방향 | **flow**: `orchestrator-service: application/OrderFulfillmentSaga` · **엔진**: `platform-core: common/saga/SagaStore·JdbcSagaStore` · **step**: 각 서비스 커맨드 핸들러(`OmsService·TmsService·BmsService`) · 계약: `contracts` |
| §7.1.8 | docker-compose 한 방·골든패스 | `docker-compose.yml` · 아래 골든패스 |

## 골든패스 — 새 이벤트/핸들러 추가 (§7.1.8)

1. `contracts/`에 record + `@EventContract` + `ContractCatalog.ALL`에 등록
2. 발행: 서비스 application 레이어에서 `events.publish(new MyEvent(...))`
3. 소비: `registry.register("<group>", MyEvent.class, this::onMyEvent)` + 핸들러
4. 사가로 엮을 땐 비즈 개발자는 step(핸들러)만 만들고, **중앙 flow 조합은 orchestrator에서**(플랫폼 오너) — 비즈 서비스는 Saga를 모른다.

## 예제라서 단순화한 것

- **사가 = 얇은 구체 오케스트레이터**(가독성): 실제는 step/flow 선언형 엔진(§7.1.7, Eventuate Tram 스타일). 원칙(중앙 flow·보상·correlationId 매칭·타임아웃·비즈 서비스 무지)은 동일.
- **Schema Registry 없음**(JSON 직접) / **eventId=randomUUID**(실제 UUIDv7) / **백오프=고정간격**(실제 지수) / **auto-create topics=on**(실제 off+IaC).
- 4 DB를 한 PostgreSQL 인스턴스에(예제 편의). 실제 DB-per-service는 인스턴스 분리 가능.
