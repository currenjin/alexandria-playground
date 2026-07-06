# Lab 09. 주문 이벤트 스트림 처리

연결 챕터: Ch.11 Stream Processing

## 목표

주문 이벤트를 처리하면서 중복, 순서 뒤바뀜, offset 문제를 재현한다.

처음에는 Kafka 없이 in-memory queue로 시작한다. 이후 Testcontainers Kafka를 붙여도 된다.

## 이벤트

```text
OrderCreated
PaymentCompleted
OrderCancelled
```

## TDD 순서

1. `OrderCreated`를 처리하면 주문 상태가 CREATED가 된다.
2. `PaymentCompleted`를 처리하면 PAID가 된다.
3. 같은 이벤트가 두 번 와도 매출은 한 번만 반영된다.
4. `PaymentCompleted`가 `OrderCreated`보다 먼저 오면 pending 처리한다.
5. consumer 재시작 후 마지막 offset부터 이어서 처리한다.

## 관찰할 문제

- at-least-once delivery
- duplicate event
- out-of-order event
- consumer offset
- exactly-once의 어려움

## 완료 조건

- 중복 이벤트 테스트가 있다.
- 순서 뒤바뀐 이벤트 테스트가 있다.
- idempotent consumer 구현 전/후 차이를 정리한다.
