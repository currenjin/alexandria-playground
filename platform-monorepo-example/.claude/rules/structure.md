# 모듈·패키지 구조

> **이 저장소는 확정 이벤트 백본의 실행 가능한 MSA 참조 구현(데모)이다.** 도메인(오더 → 배차 → 배송 → 정산)은 백본을 실증하는 **수단**이지 실제 제품의 OMS/TMS/BMS 설계가 아니다 — 도메인 모델을 여기서 가져다 쓰지 말 것.

> 패키지 루트는 모두 `com.wemeet.*`다(중간 `eventbackbone` 레벨 없음). core는 `common` 레벨 없이 `com.wemeet.core` 바로 아래로 평탄하다.

- `platforms/common` (`com.wemeet.common`) — 순수 유틸 모듈(placeholder). 도메인·프레임워크 의존 0. 지금은 `package-info`만.
- `platforms/contract` (`com.wemeet.contract`) — 이벤트·커맨드 record + `@EventContract`. 서비스끼리 서로 의존하지 않고 이 모듈에만 의존한다.
- `platforms/core` (`com.wemeet.core`) — 백본 라이브러리(도메인 흔적 0). 루트에 `@EnableEventBackbone`·`EventBackboneConfig`.
  - `core/event/contract` — `DomainEvent`·`EventContract`·`Envelope`·`EventTypes`·`UuidV7` (계약·공유 어휘)
  - `core/event/publish` — `EventPublisher`·`OutboxEventPublisher`
  - `core/event/consume` — `EventConsumerSupport`·`@EventHandler`·`EventHandlerRegistrar`·`HandlerRegistry`
  - `core/event/config`·`transport` — 토픽 카탈로그, 브로커 어댑터(kafka/inmemory/sqs)
  - `core/{outbox,inbox,saga,context,maintenance}`
- `services/oms`·`services/tms`·`services/bms`·`services/ems` (`com.wemeet.{oms,tms,bms,ems}`) — 도메인 서비스. 레이어드(`presentation`·`application`·`domain`·`infrastructure`).
- `platforms/orchestrator` (`com.wemeet.orchestrator`) — 중앙 사가(프로세스 매니저). 컨트롤러 없음, `saga_instance`로 흐름 추적.

## 활성화

- 앱은 `@SpringBootApplication` + `@EnableEventBackbone`. `@EnableEventBackbone`을 안 붙이면 백본 빈이 하나도 안 올라온다(의존성만으로 몰래 켜지지 않는다).
- 소비는 `platform.events.subscribe-topics`를 선언하고 모듈이 자기 `*EventListener`를 가져야 컨슈머가 생긴다. 발행 전용 서비스는 컨슈머가 0.
- 토픽은 발행 서비스가 자기 `application.yml`의 `platform.events.topics`에 선언·소유한다(소비자는 구독만).

## 레이어 의존 규칙

각 서비스는 레이어드다. 위에서 아래로만 의존하고, 건너뛰기·역방향은 금지한다.

| 레이어 | 역할 | 허용 의존 | 금지 |
| --- | --- | --- | --- |
| `presentation` | Controller·EventListener(진입점) | `application` | `domain`·`infrastructure` 직접 호출 |
| `application` | Service(유스케이스) | `domain`, 도메인 포트 | 다른 서비스 직접 호출 |
| `domain` | 애그리거트·값·포트 인터페이스 | (없음, 순수) | 상위 레이어·Spring 의존 |
| `infrastructure` | 포트 구현(리포지토리 등) | `domain` | `application`·`presentation` |

- 크로스서비스는 레이어가 아니라 이벤트/커맨드(`platforms/contract`)로만 넘나든다.
