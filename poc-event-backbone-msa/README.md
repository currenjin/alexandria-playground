# poc-event-backbone-msa

이벤트 기반 MSA의 **이벤트 백본**(봉투 · Outbox · 폴링 릴레이 · Inbox 멱등 · DLT · Saga)을 실제로 구현·검증한 예제.
**OMS·TMS·BMS를 각각 독립 배포되는 서비스**로 나누고, Kafka 이벤트로만 통신한다. 각 서비스는 **레이어드 아키텍처**(api / application / domain / infrastructure).

> 본문에 나오는 절 번호(§7.1.x)는 이 예제의 설계 근거가 된 내부 설계 문서의 "이벤트 백본" 챕터를 가리키는 라벨이다.

## 모듈 구조

```
event-backbone-example/
├── contracts/        # 공유 계약: 이벤트·커맨드 record + @EventContract (Spring 무의존)
├── platform-core/    # 공통 인프라: 봉투·EventPublisher·Outbox 릴레이·Inbox 소비지원·DLT (라이브러리)
├── oms-service/      # 주문 + 주문이행 사가(흐름 주인). REST 진입점. DB=oms
├── tms-service/      # 배차. DB=tms
└── bms-service/      # 정산. DB=bms
```

- **의존 방향(§7.1.7)**: 모든 서비스 → `platform-core` → `contracts`. **서비스끼리는 서로 의존하지 않는다.** 사가가 `TripDispatched`를 참조해도 TMS 모듈이 아니라 공유 `contracts`에 의존.
- 각 서비스 = 독립 Spring Boot 앱 + **자기 DB**(DB-per-service) + 자기 outbox/inbox. 통신은 Kafka로만.
- 레이어드: `api`(REST·Kafka 인바운드 어댑터) → `application`(유스케이스·사가) → `domain`(엔티티·리포지토리 포트) → `infrastructure`(JDBC 어댑터).

## 실행 (MSA 한 방)

```bash
docker compose up --build      # Kafka + PostgreSQL(3 DB) + OMS·TMS·BMS 3개 서비스

# 정상 흐름 — 주문→배차→정산 (서비스 간 Kafka 이벤트로)
curl -XPOST "http://localhost:8080/demo/orders?amount=1250000"
curl "http://localhost:8080/demo/state/ORD-xxxx"      # order=CONFIRMED, saga=COMPLETED

# 보상 흐름 — 배차 불가(amount=0) → 사가가 주문 취소
curl -XPOST "http://localhost:8080/demo/orders?amount=0"
curl "http://localhost:8080/demo/state/ORD-yyyy"      # order=CANCELLED, saga=COMPENSATED
```

로컬(도커 인프라만) 개발: `docker compose up kafka postgres` 후 각 서비스를 `./gradlew :oms-service:bootRun` 등으로.

## MSA 이벤트 통신 흐름

```
POST /demo/orders (OMS)
  OMS.confirm → orders + oms.outbox INSERT (한 트랜잭션) → 릴레이 → Kafka: oms.order/oms.order.confirmed
  [saga@OMS] on order.confirmed → saga_instance STARTED → publish → Kafka: tms.cmd/create_trip
  [TMS] on create_trip → trips DISPATCHED → publish → Kafka: tms.trip/dispatched
  [saga@OMS] on trip.dispatched → publish → Kafka: bms.cmd/schedule_settlement
  [BMS] on schedule_settlement → settlements SCHEDULED → publish → Kafka: bms.settlement/scheduled
  [saga@OMS] on settlement.scheduled → COMPLETED ✅

보상(amount=0): [TMS] create_trip → trip.creation_failed → [saga] CancelOrder → [OMS] orders CANCELLED → COMPENSATED
```

각 서비스는 자기 DB에만 쓰고, 다른 서비스의 결과는 Kafka 이벤트로만 안다 — 진짜 MSA.

## 코드 ↔ 확정 설계(§7.1.x)

| 절 | 개념 | 코드 |
| --- | --- | --- |
| §7.1.1 | 봉투 8필드·논리명 eventType·컨텍스트 자동 | `platform-core: Envelope · EventContract · OutboxEventPublisher · FlowContext` |
| §7.1.2 | 토픽=애그리거트당(앞 두 마디) | `EventTypes.topicOf()` |
| §7.1.3 | Outbox 하이브리드·트랜잭션 밖 publish=예외 | `platform-core: OutboxEventPublisher` · `V1__outbox_inbox.sql` |
| §7.1.4 | 폴링 릴레이·at-least-once | `platform-core: OutboxRelay` (각 서비스가 자기 outbox 릴레이) |
| §7.1.5 | 컨슈머 그룹·Inbox 멱등·같은 트랜잭션 | `EventConsumerSupport` · 각 서비스 `*EventListener`(그룹별) |
| §7.1.6 | 재시도→DLT(컨슈머가 produce) | `EventInfraConfig`(DefaultErrorHandler + DeadLetterPublishingRecoverer) |
| §7.1.7 | 사가 step(도메인)/flow(중앙)·보상·타임아웃·계약 의존 한 방향 | `oms: application/OrderFulfillmentSaga`(flow) · 각 서비스 커맨드 핸들러(step) · `contracts`(공유 계약) |
| §7.1.8 | docker-compose 한 방·골든패스 | `docker-compose.yml` · 아래 골든패스 |

## 골든패스 — 새 이벤트/핸들러 추가 (§7.1.8)

1. `contracts/`에 record + `@EventContract` + `ContractCatalog.ALL`에 등록
2. 발행: 서비스 application 레이어에서 `events.publish(new MyEvent(...))`
3. 소비: `registry.register("<group>", MyEvent.class, this::onMyEvent)` + 핸들러

## 예제라서 단순화한 것

- **사가 = 구체 오케스트레이터**(가독성): 실제는 step/flow 선언형 엔진(§7.1.7). 원칙(중앙 flow·보상·correlationId 매칭·타임아웃)은 동일.
- **Schema Registry 없음**(JSON 직접) / **eventId=randomUUID**(실제 UUIDv7) / **백오프=고정간격**(실제 지수) / **auto-create topics=on**(실제 off+IaC). 모두 §7.1.x의 실제 스펙은 문서 참조.
- 3 DB를 한 PostgreSQL 인스턴스에(예제 편의). 실제 DB-per-service는 인스턴스 분리 가능.
