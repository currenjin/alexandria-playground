# event-backbone-example

확정 이벤트 백본(Outbox·릴레이·Inbox·DLQ·사가)의 **실행 가능한 MSA 참조 구현**이다. Java 21 · Spring Boot 3.3.5 · Kafka · PostgreSQL · Flyway · Testcontainers.

## 무엇이고 무엇이 아닌가

- 이벤트 백본 라이브러리(platforms/core)와, 그걸 쓰는 4개 서비스를 담은 데모다.
- 도메인(주문 → 배차 → 정산)은 백본을 실증하려는 **데모 수단**이다. 실제 제품의 OMS/TMS/BMS 설계가 아니다 — 도메인 모델을 여기서 가져다 쓰지 말 것.

## 실행

```
docker compose up --build          # Kafka + PostgreSQL + 4서비스
```

왕복(골든패스): `POST :8080/demo/orders` → `POST :8081/demo/dispatches?orderId=<id>` → `POST :8081/demo/dispatches/<dispatchId>/deliver` → `GET :8080/demo/state/<orderId>` = `SETTLED`. 포트: oms 8080 · tms 8081 · bms 8082 · orchestrator 8083. 테스트 `./gradlew test`.

## 세부 규칙 (`.claude/rules/`, 자동 로드)

- `structure.md` — 모듈·패키지 구조, 활성화(`@EnableEventBackbone`)
- `conventions.md` — 코드 수정 규칙(의존·발행/소비·권위)

## 문서

- `README.md` — 개요와 흐름
- `GUIDE.md` — 백본을 쓰는 개발자용(이벤트·사가 추가법)
