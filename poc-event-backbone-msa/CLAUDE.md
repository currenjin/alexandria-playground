# event-backbone-example

확정 이벤트 백본(Outbox·릴레이·Inbox·DLQ·사가)의 **실행 가능한 MSA 참조 구현**이다. Java 21 · Spring Boot 3.3.5 · Kafka · PostgreSQL · Flyway · Testcontainers.

## 무엇이고 무엇이 아닌가

- 이벤트 백본 라이브러리(`platforms/core`)와, 그걸 쓰는 도메인 서비스 4개(oms·tms·bms·ems) + 중앙 사가(`platforms/orchestrator`)를 담은 데모다.
- 도메인(오더 → 배차 → 배송 → 정산)은 백본을 실증하려는 **데모 수단**이다. 실제 제품의 OMS/TMS/BMS 설계가 아니다 — 도메인 모델을 여기서 가져다 쓰지 말 것.

## 구조 (Gradle 멀티모듈 모노레포)

- `services/{oms,tms,bms,ems}` — 도메인 서비스(각 독립 Spring Boot 앱 + 자기 DB). 레이어드: `presentation`·`application`·`domain`·`infrastructure`.
- `platforms/{core,common,contract,orchestrator,monitor}` — 공통 레이어. `core`=백본 라이브러리, `contract`=이벤트·커맨드 계약, `orchestrator`=중앙 사가(컨트롤러 없음), `common`=순수 유틸(placeholder), `monitor`=라이브 대시보드(Gradle 모듈 아님).
- 패키지 루트는 모두 `com.wemeet.*`(중간 `eventbackbone` 없음). core는 `com.wemeet.core` 바로 아래로 평탄(`common` 레벨 없음), contract=`com.wemeet.contract`, orchestrator=`com.wemeet.orchestrator`, 서비스=`com.wemeet.{oms,tms,bms,ems}`.

## 실행

```
docker compose up --build          # Kafka + PostgreSQL(5 DB) + 서비스 5개(orchestrator·oms·tms·bms·ems)
```

왕복(골든패스): `POST :8080/demo/orders` → `POST :8081/demo/dispatches?orderId=<id>` → `POST :8081/demo/dispatches/<dispatchId>/deliver` → `GET :8080/demo/state/<orderId>` = `SETTLED`. 포트: oms 8080 · tms 8081 · orchestrator 8083 · ems 8084 (bms는 호스트 포트 미노출·컨테이너 내부 8082). 테스트 `./gradlew test`.

## 세부 규칙 (`.claude/rules/`, 자동 로드)

- `structure.md` — 모듈·패키지 구조, 활성화(`@EnableEventBackbone`)
- `conventions.md` — 코드 수정 규칙(의존·발행/소비·권위)

## 문서

- `README.md` — 개요와 흐름
- `GUIDE.md` — 백본을 쓰는 개발자용(이벤트·사가 추가법)
