# 코드 수정 규칙

> 이 rules는 이 예제의 코드 규칙이다.

- 서비스끼리 직접 의존·직접 DB 접근 금지. 크로스서비스는 이벤트/커맨드(`platforms/contract`)로만.
- 발행 = `eventPublisher.publish(event)` 한 줄, 반드시 `@Transactional` 안(= Outbox INSERT). 소비 = `@EventHandler` 메소드 하나.
- 봉투 8필드·멱등(Inbox)·재시도/DLT·테넌트 컨텍스트 전파는 공통이 처리한다. 비즈 코드에서 건드리지 말 것.
- 상태 전이 권위 = 애그리거트(예: OMS 오더). 사가는 커맨드로 "전이 시도"하고, 유효성은 애그리거트가 낙관적 잠금 + 가드로 판정한다.
- 새 이벤트/사가 추가법은 `docs/GUIDE.md` 골든패스를 따른다.

## 불변성

- 이벤트·커맨드는 `record`(불변)로 정의한다.
- 도메인 상태는 제자리 변형이 아니라 가드된 전이로 바꾼다(애그리거트가 유효성을 판정하고, 유효하면 성공 이벤트·아니면 거부 이벤트).

## 로깅

- SLF4J 로거를 쓴다. `System.out`·`println` 금지.
- 레벨: `ERROR`(복구 불가·즉시 알림) / `WARN`(복구 가능하나 점검 필요) / `INFO`(주요 비즈 이벤트·외부연동 성공실패) / `DEBUG`(개발·트러블슈팅, prod 기본 OFF).

## 데이터 접근 (ORM)

- 도메인·application은 Repository **포트(인터페이스)**만 안다 — ORM 존재를 모른다. 인프라 어댑터가 JPA/MyBatis로 구현.
- **한 애그리거트 = 단일 ORM**(혼용 금지). 마스터·공통 = JPA / 복잡 조회·업무·집계 = MyBatis.
- 단일 DataSource + 단일 TransactionManager로 한 `@Transactional` 공유. 같은 TX에서 JPA로 쓰고 MyBatis로 읽으면 flush 전이라 못 본다 → 경계 전 flush, 또는 유스케이스당 한 ORM.

## 모듈 간 통신

- 크로스서비스 기본 = 이벤트/커맨드(`platforms/contract`). **즉시 응답·강한 정합·조회**가 꼭 필요할 때만 동기 **gRPC**(제품: Spring gRPC). 도메인은 gRPC 스텁을 직접 보지 않고 인프라 어댑터 뒤로 감춘다.

## 안티패턴 (하지 말 것)

- 서비스 간 **직접 의존·직접 DB 접근** (크로스는 `contract`만)
- **트랜잭션 밖 publish** (이중 쓰기 위험)
- **애그리거트 밖에서 상태 전이** — 사가가 상태를 직접 바꾸지 말 것(커맨드로 "시도", 판정은 애그리거트가 가드+낙관락으로)
- **한 애그리거트에 ORM 혼용** (JPA+MyBatis 섞기)
- 이벤트 금액에 **부동소수** (문자열 + currency)
- **같은 (그룹, 타입)에 `@EventHandler` 둘** (기동 fail-fast로 차단됨)
- **`contract`에 유틸 / `common`에 이벤트** (경계 오염 — `platforms/contract`=규약, `platforms/common`=순수 유틸, `platforms/core`=백본 인프라)
- `platform.events.*`·토픽 **하드코딩** (설정 주입)
- 레이어 **건너뛰기·역방향** — `presentation → application → domain ← infrastructure`만
