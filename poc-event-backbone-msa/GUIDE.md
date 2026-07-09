# 비즈니스 개발자 가이드

이 백본 위에서 기능을 만들 때 **당신이 할 일은 아주 적다.** 발행·소비의 안전장치(outbox·inbox·재시도·DLT·컨텍스트·토픽)는 전부 공통(`platform-core`)이 처리한다.

---

## TL;DR — 당신이 하는 일은 이 셋

1. **이벤트/커맨드 정의** — `contracts`에 record + `@EventContract` 한 줄
2. **발행** — 유스케이스 안에서 `events.publish(new MyEvent(...))`
3. **소비** — 핸들러 메소드에 `@EventHandler` 한 줄 (공통이 자동 등록)

그게 전부다. 아래는 각각의 실제 예시.

---

## 1. 이벤트 정의 (contracts)

`contracts` 모듈에 record로 선언한다. `@EventContract`의 type이 논리 이벤트명이다(토픽·직렬화는 이걸로 자동).

```java
@EventContract(type = "oms.order.created", version = 1)
public record OrderCreated(String orderId, String shipperId, String origin, String destination,
                           String amount, String currency)
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
public void create(String orderId, String shipperId, String origin, String destination,
                   String amount, String currency) {
    orders.save(Order.create(orderId, shipperId, origin, destination, amount, currency));
    events.publish(new OrderCreated(orderId, shipperId, origin, destination, amount, currency));
}
```

- 토픽 이름, envelope 8필드(eventId·시간·tenant·correlationId…)는 **공통이 알아서** 채운다.
- 트랜잭션 밖에서 부르면 예외가 난다(일부러 — 이중 쓰기 방지).

## 3. 이벤트/커맨드 소비

메소드에 `@EventHandler`만 붙이면 **공통이 자동 등록**한다(이벤트 타입은 파라미터에서, 그룹은 서비스 설정 `platform.events.consumer-group`에서). 등록·`@KafkaListener`·멱등·트랜잭션은 전부 공통.

```java
@EventHandler
void onCreateSettlement(CreateSettlement cmd) {
    // 비즈 로직만. 등록·중복 방지·컨텍스트·트랜잭션은 공통이 처리.
    settlements.save(new Settlement(...));
    events.publish(new SettlementCompleted(...));   // 후속 사실도 그냥 publish
}
```

## 4. 흐름(사가)에 참여하려면

당신은 **사가(Saga) 엔진을 몰라도 된다.** 두 가지 중 하나만 하면 흐름 조합은 중앙 orchestrator(플랫폼 오너)가 한다:

- **자기 API로 행위하고 사실을 발행** — 예: TMS가 `/dispatches`로 배차하고 `DispatchCreated` 발행.
- **orchestrator가 보낸 커맨드를 처리** — 예: OMS가 `MarkDispatched`를 받아 오더 상태를 DISPATCHED로.

```java
// 사실 발행 (TMS — 자기 API로 배차)
public String dispatch(String orderId) { ... ; events.publish(new DispatchCreated(...)); }

// 커맨드 처리 (OMS — orchestrator가 상태 맞추라고 보낸 것)
@EventHandler void onMarkDispatched(MarkDispatched cmd) { orders.updateStatus(cmd.orderId(), DISPATCHED); }
```

→ "어떤 사실에 어떤 후속이 이어진다"는 조합만 orchestrator에 정의하면 끝. 참여자는 자기가 어느 흐름에 속하는지 모른다.

> **사가 클래스는 "프로세스(비즈 목적) 단위로 하나"** — 오더 이행은 `OrderFulfillmentSaga` 하나가 모든 경로(정상완주·배차취소→복귀·재배차·취소·거부)를 분기로 처리한다. 시나리오마다 쪼개지 않는다(한 오더의 상태를 여러 사가가 나눠 가지면 일관성이 깨짐). 별도 사가는 **다른 종류의 프로세스**(반품·클레임·정산마감 등)가 생길 때 각각 클래스로 추가한다.

## 신경 쓰지 않아도 되는 것

outbox · relay · inbox 멱등 · 재시도/DLT · 토픽 이름·파티션 · correlationId 전파 · tenant 컨텍스트 — **전부 `platform-core`가 강제**한다. 당신 코드에는 안 보인다.

---
---

# 내부는 이렇게 돌아간다 (궁금하면)

위만 알면 개발은 된다. 아래는 "그래서 내부적으로 무슨 일이 일어나나"가 궁금할 때.

## 발행: publish() → outbox → relay → Kafka

- `events.publish(e)` = `OutboxEventPublisher`가 envelope를 조립해 **같은 트랜잭션의 `outbox` 테이블에 INSERT**(§7.1.3). Kafka로 직접 안 쏜다.
- `OutboxRelay`(@Scheduled 200ms)가 미발행 행을 `seq` 순서·`FOR UPDATE SKIP LOCKED`로 읽어 Kafka에 발행하고 `published_at` 마킹(§7.1.4). 배치 500. 발행은 `MessageTransport` 포트 경유(브로커 교체 지점).
- eventId는 **UUIDv7**, 토픽은 eventType 앞 두 마디(`EventTypes.topicOf`).

## 소비: @KafkaListener → consume() → inbox → 핸들러

- 소비하는 서비스는 자기 `*EventListener`(@KafkaListener, 그룹별)가 공통 `EventConsumerSupport.consume(group, envelope)`를 부른다. (사실만 발행하고 커맨드를 안 받는 서비스는 리스너가 없다 — 예: TMS는 publish-only.)
- consume이 envelope 파싱 → `InboxRepository`의 `INSERT … ON CONFLICT DO NOTHING`(중복이면 skip) → 컨텍스트 복원 → 등록된 핸들러 호출(§7.1.5).
- **inbox 기록 + 당신의 도메인 쓰기 + 후속 publish가 한 트랜잭션.** 핸들러가 실패하면 통째 롤백돼 재처리된다.

## 사가: 중앙 orchestrator가 flow를 지휘

- 참여자(당신)는 자기 API·핸들러만. 흐름 정의(`OrderFulfillmentSaga`)는 orchestrator 서비스에 있다(§7.1.7). orchestrator엔 컨트롤러가 없다 — 이벤트만 소비해 커맨드로 코디네이션한다.
- orchestrator가 사실 이벤트를 받아 다음 단계 커맨드(`Mark*`·`CreateSettlement`)를 발행하며 상태를 진전시킨다. 되돌림은 자동 보상이 아니라 **취소 흐름**(배차취소→`MarkUndispatched`→오더 미배차 복귀)으로.
- 한 흐름을 잇는 열쇠 = **orderId(업무 키)**. 배차·배송·취소가 여러 HTTP 요청으로 나뉘어도 orderId로 같은 프로세스에 붙는다. (요청별 correlationId는 추적용 별개.)
- 가드: 프로세스가 기대 상태보다 **이전**이면 이벤트를 무시하지 않고 재시도(최종일관성 self-heal), **이후/종료**면 무시(중복·종료 방어).

## 컨텍스트: 진입점에서 자동 주입

- HTTP는 `FlowContextFilter`가, Kafka는 공통 리스너가 tenant·corp·correlationId를 컨텍스트에 넣고 정리한다.
- 그래서 발행 때 공통이 그 값을 envelope에 자동 복사 → 당신은 `publish(event)`만.

## 실패: 재시도 → DLT

- 핸들러 예외는 인메모리 **지수 백오프(1s→4s→16s)** 후 소진되면 `<원본>.DLT`로 격리(§7.1.6). 컨슈머가 직접 produce.
- 역직렬화 실패처럼 재시도가 무의미한 건 즉시 DLT.

---

더 깊은 근거·수치는 `design/framework/공통-확정.md §7.1`, 실제 흐름은 위키의 "🧪 코드 관통 시뮬레이션"에서.
