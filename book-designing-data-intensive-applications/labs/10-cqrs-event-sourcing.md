# Lab 10. CQRS + Event Sourcing

연결 챕터: Ch.12 The Future of Data Systems

## 목표

쓰기 모델은 이벤트를 저장하고, 읽기 모델은 이벤트 projection으로 만든다.

## 도메인

```text
Order aggregate
```

이벤트:

```text
OrderCreated
ItemAdded
OrderPaid
```

읽기 모델:

```text
order_summary_view
```

## TDD 순서

1. 주문 생성 command는 `OrderCreated` 이벤트를 만든다.
2. 상품 추가 command는 `ItemAdded` 이벤트를 만든다.
3. 결제 command는 `OrderPaid` 이벤트를 만든다.
4. 이벤트를 처음부터 재생하면 read model이 복구된다.
5. projection이 중간에 실패해도 다시 재처리할 수 있다.
6. 같은 이벤트를 두 번 projection해도 결과가 중복되지 않는다.

## 구현 단계

1. in-memory event store
2. file-backed event store
3. read model projection
4. projection failure 재현
5. replay/rebuild 기능

## 완료 조건

- event replay로 read model을 복구하는 테스트가 있다.
- projection idempotency 테스트가 있다.
- derived data가 원천 데이터와 달라질 수 있는 지점을 적는다.
