# POC: Apache Seata (AT 모드)

`@GlobalTransactional`을 사용해 주문 생성 + 잔액 차감을 하나의 글로벌 트랜잭션으로 묶는 최소 예제입니다.

## 시나리오

1. `OrderService.placeOrder()`가 글로벌 트랜잭션 시작
2. `AccountService.decreaseBalance()`로 잔액 차감
3. `OrderRepository.create()`로 주문 생성
4. 중간에 실패 발생 시 Seata가 브랜치 트랜잭션 전체 롤백

## 구성

- Spring Boot 3 + Kotlin
- MySQL
- Seata AT (`io.seata:seata-spring-boot3-starter`)

## 실행 전 준비

### 1) MySQL 실행

```bash
docker compose up -d
```

### 2) Seata Server 실행

Seata 서버를 로컬에서 `127.0.0.1:8091`로 실행해야 합니다.

- `src/main/resources/application.yml`의 `seata.service.grouplist.default`가 `127.0.0.1:8091`로 설정되어 있습니다.
- 이미 실행 중인 Seata 서버가 있다면 그대로 사용하면 됩니다.

## 실행

```bash
./gradlew bootRun
```

## 테스트 요청

### 성공 케이스

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"accountId":1,"amount":1000,"forceFail":false}'
```

### 실패(롤백) 케이스

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"accountId":1,"amount":1000,"forceFail":true}'
```

`forceFail=true`로 호출하면 주문 insert와 잔액 차감이 모두 롤백되는 것이 목표 동작입니다.

## 핵심 코드

- 글로벌 트랜잭션: `src/main/kotlin/com/currenjin/seata/order/OrderService.kt`
- 브랜치 트랜잭션: `src/main/kotlin/com/currenjin/seata/account/AccountService.kt`
- `undo_log` 테이블: `src/main/resources/schema.sql`
