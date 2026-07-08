# 비즈니스 개발자 가이드

이 백본 위에서 기능을 만들 때 **당신이 할 일은 아주 적다.** 발행·소비의 안전장치(outbox·inbox·재시도·DLT·컨텍스트·토픽)는 전부 공통(`platform-core`)이 처리한다.

---

## TL;DR — 당신이 하는 일은 이 셋

1. **이벤트/커맨드 정의** — `contracts`에 record + `@EventContract` 한 줄
2. **발행** — 유스케이스 안에서 `events.publish(new MyEvent(...))`
3. **소비** — `registry.register("<그룹>", MyEvent.class, this::onMyEvent)` + 핸들러 메소드

그게 전부다. 아래는 각각의 실제 예시.

---

## 1. 이벤트 정의 (contracts)

`contracts` 모듈에 record로 선언한다. `@EventContract`의 type이 논리 이벤트명이다(토픽·직렬화는 이걸로 자동).

```java
@EventContract(type = "oms.order.confirmed", version = 1)
public record OrderConfirmed(String orderId, String customerId, String amount, String currency)
        implements DomainEvent {
    @Override public String aggregateId() { return orderId; }   // 파티션 키 = "어느 건의 이벤트인가"
}
```

- 금액 같은 값은 **문자열 + currency**로(부동소수 금지).
- 만든 record를 `ContractCatalog.ALL`에 등록만 하면 된다.

## 2. 이벤트 발행

application 레이어(유스케이스)에서 도메인 변경과 **같은 `@Transactional` 안**에서 `events.publish(...)`를 부른다. 끝.

```java
@Transactional
public void confirm(String orderId, String customerId, String amount, String currency) {
    orders.save(Order.confirm(orderId, customerId, amount, currency));
    events.publish(new OrderConfirmed(orderId, customerId, amount, currency));
}
```

- 토픽 이름, envelope 8필드(eventId·시간·tenant·correlationId…)는 **공통이 알아서** 채운다.
- 트랜잭션 밖에서 부르면 예외가 난다(일부러 — 이중 쓰기 방지).

## 3. 이벤트/커맨드 소비

핸들러를 등록하고 메소드만 쓴다. `@KafkaListener`·멱등·트랜잭션은 서비스에 이미 배선돼 있다.

```java
@PostConstruct
void register() {
    registry.register("tms", CreateTrip.class, this::onCreateTrip);
}

void onCreateTrip(CreateTrip cmd) {
    // 비즈 로직만. 중복 방지·컨텍스트·트랜잭션은 공통이 처리.
    trips.save(new Trip(...));
    events.publish(new TripDispatched(...));   // 후속 이벤트도 그냥 publish
}
```

- `"tms"` = 이 서비스의 컨슈머 그룹 이름.

## 4. 사가에 참여하려면 (step만)

당신은 **사가(Saga)를 몰라도 된다.** "할 일" 핸들러와 "되돌릴 일"(보상) 핸들러만 만들면, 흐름 조합은 중앙 orchestrator(플랫폼 오너)가 한다.

```java
void onCreateTrip(CreateTrip cmd) { /* 배차 */ }      // 할 일
void onCancelTrip(CancelTrip cmd) { /* 배차 취소 */ }  // 되돌릴 일(보상)
```

→ "이 두 커맨드가 한 흐름에 엮인다"만 플랫폼 오너에게 알려주면 끝.

## 신경 쓰지 않아도 되는 것

outbox · relay · inbox 멱등 · 재시도/DLT · 토픽 이름·파티션 · correlationId 전파 · tenant 컨텍스트 — **전부 `platform-core`가 강제**한다. 당신 코드에는 안 보인다.

---
---

# 내부는 이렇게 돌아간다 (궁금하면)

위만 알면 개발은 된다. 아래는 "그래서 내부적으로 무슨 일이 일어나나"가 궁금할 때.

## 발행: publish() → outbox → relay → Kafka

- `events.publish(e)` = `OutboxEventPublisher`가 envelope를 조립해 **같은 트랜잭션의 `outbox` 테이블에 INSERT**(§7.1.3). Kafka로 직접 안 쏜다.
- `OutboxRelay`(@Scheduled 200ms)가 미발행 행을 `seq` 순서·`FOR UPDATE SKIP LOCKED`로 읽어 Kafka에 발행하고 `published_at` 마킹(§7.1.4). 배치 500.
- eventId는 **UUIDv7**, 토픽은 eventType 앞 두 마디(`EventTypes.topicOf`).

## 소비: @KafkaListener → consume() → inbox → 핸들러

- 각 서비스의 `*EventListener`(@KafkaListener, 그룹별)가 공통 `EventConsumerSupport.consume(group, envelope)`를 부른다.
- consume이 envelope 파싱 → `InboxRepository`의 `INSERT … ON CONFLICT DO NOTHING`(중복이면 skip) → 컨텍스트 복원 → 등록된 핸들러 호출(§7.1.5).
- **inbox 기록 + 당신의 도메인 쓰기 + 후속 publish가 한 트랜잭션.** 핸들러가 실패하면 통째 롤백돼 재처리된다.

## 사가: 중앙 orchestrator가 flow를 지휘

- 참여자(당신)는 커맨드 핸들러(step)만. 흐름 정의(`OrderFulfillmentSaga`)는 orchestrator 서비스에 있다(§7.1.7).
- orchestrator가 커맨드를 발행하고 참여자의 결과 이벤트를 받아 다음 단계로. 실패 이벤트나 타임아웃이면 보상 커맨드를 보낸다.
- 한 흐름을 잇는 열쇠 = **correlationId**(운영자가 이거 하나로 전 여정 추적).

## 컨텍스트: 진입점에서 자동 주입

- HTTP는 `FlowContextFilter`가, Kafka는 공통 리스너가 tenant·corp·correlationId를 컨텍스트에 넣고 정리한다.
- 그래서 발행 때 공통이 그 값을 envelope에 자동 복사 → 당신은 `publish(event)`만.

## 실패: 재시도 → DLT

- 핸들러 예외는 인메모리 **지수 백오프(1s→4s→16s)** 후 소진되면 `<원본>.DLT`로 격리(§7.1.6). 컨슈머가 직접 produce.
- 역직렬화 실패처럼 재시도가 무의미한 건 즉시 DLT.

---

더 깊은 근거·수치는 `design/framework/공통-확정.md §7.1`, 실제 흐름은 위키의 "🧪 코드 관통 시뮬레이션"에서.
