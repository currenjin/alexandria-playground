# Lab 02. 계좌 이체와 트랜잭션 이상 현상

연결 챕터: Ch.7 Transactions

## 목표

계좌 이체 시스템을 만들고 동시성에서 총액 불변식이 깨지는 상황을 재현한다.

## 도메인

```text
Account(id, balance, version)
Transfer(fromAccountId, toAccountId, amount)
```

API 예시:

```text
POST /transfer
GET /accounts/{id}
```

## TDD 순서

1. 단일 이체 후 출금/입금 계좌의 잔액이 바뀐다.
2. 잔액보다 큰 금액은 이체할 수 없다.
3. 동시에 100번 이체해도 전체 잔액 합계는 보존되어야 한다.
4. 같은 계좌에서 동시에 출금해도 잔액은 음수가 되면 안 된다.
5. optimistic lock 충돌 시 재시도하거나 실패로 기록한다.

## 일부러 깨뜨릴 것

- lost update
- dirty read
- non-repeatable read
- write skew

## 개선 단계

1. 트랜잭션 없이 구현한다.
2. DB transaction을 추가한다.
3. isolation level을 바꿔본다.
4. pessimistic lock을 적용한다.

```sql
SELECT * FROM account WHERE id = ? FOR UPDATE;
```

5. optimistic lock을 적용한다.

```text
version 컬럼 비교 후 update
```

## 완료 조건

- 깨지는 테스트와 고친 테스트가 둘 다 남아 있다.
- 어떤 격리 수준에서 어떤 이상 현상이 가능한지 표로 정리한다.
- `SELECT ... FOR UPDATE`와 `version` 방식의 장단점을 5문장 안에 비교한다.
