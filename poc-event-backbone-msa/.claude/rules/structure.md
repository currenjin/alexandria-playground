# 모듈·패키지 구조

- `contracts` — 이벤트·커맨드 record + `@EventContract`. 서비스끼리 서로 의존하지 않고 이 모듈에만 의존한다.
- `platform-core` — 백본 라이브러리(도메인 흔적 0).
  - `common/event/contract` — `DomainEvent`·`EventContract`·`Envelope`·`EventTypes`·`UuidV7` (계약·공유 어휘)
  - `common/event/publish` — `EventPublisher`·`OutboxEventPublisher`
  - `common/event/consume` — `EventConsumerSupport`·`@EventHandler`·`EventHandlerRegistrar`·`HandlerRegistry`
  - `common/event/config`·`transport` — 토픽 카탈로그, 브로커 어댑터(kafka/inmemory/sqs)
  - `common/{outbox,inbox,saga,context,maintenance}`
- `oms-service`·`tms-service`·`bms-service` — 도메인 서비스. 레이어드(`api`·`application`·`domain`·`infrastructure`).
- `orchestrator-service` — 중앙 사가(프로세스 매니저). 컨트롤러 없음, `saga_instance`로 흐름 추적.

## 활성화

- 앱은 `@SpringBootApplication` + `@EnableEventBackbone`. `@EnableEventBackbone`을 안 붙이면 백본 빈이 하나도 안 올라온다(의존성만으로 몰래 켜지지 않는다).
- 소비는 `platform.events.subscribe-topics`를 선언하고 모듈이 자기 `*EventListener`를 가져야 컨슈머가 생긴다. 발행 전용 서비스는 컨슈머가 0.
- 토픽은 발행 서비스가 자기 `application.yml`의 `platform.events.topics`에 선언·소유한다(소비자는 구독만).
