# 코드 수정 규칙

- 서비스끼리 직접 의존·직접 DB 접근 금지. 크로스서비스는 이벤트/커맨드(`contracts`)로만.
- 발행 = `eventPublisher.publish(event)` 한 줄, 반드시 `@Transactional` 안(= Outbox INSERT). 소비 = `@EventHandler` 메소드 하나.
- 봉투 7필드·멱등(Inbox)·재시도/DLT·테넌트 컨텍스트 전파는 공통이 처리한다. 비즈 코드에서 건드리지 말 것.
- 상태 전이 권위 = 애그리거트(예: OMS 오더). 사가는 커맨드로 "전이 시도"하고, 유효성은 애그리거트가 낙관적 잠금 + 가드로 판정한다.
- 새 이벤트/사가 추가법은 `GUIDE.md` 골든패스를 따른다.
