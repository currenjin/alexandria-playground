### Q1. 격리 수준(Isolation Level)은 어떻게 선택하나요?
A. 대부분 RR/SI(Repeatable Read/Snapshot Isolation)이 기본 균형점. Write Skew 위험 경로만 Serializable 또는 명시적 락. RC(Read Committed)는 단순 조회/보고용. 성능↔안전 트레이드오프.

### Q2. Write Skew란 무엇이고 어떻게 방지하나요?
A. 두 트랜잭션이 각각 조건 검사 후 쓰기로 불변식 위반. 예: "최소 1명 당직" 확인 후 동시 퇴근. 방지: Serializable, SELECT FOR UPDATE(락), 원자적 카운터/제약, 큐로 직렬화.

### Q3. SELECT FOR UPDATE는 언제 쓰나요?
A. 읽은 행을 트랜잭션 끝까지 다른 트랜잭션이 수정 못하게 락. Lost Update 방지, 재고 차감 등. 주의: 락 범위 최소화(인덱스 활용), 교착 방지(락 순서 통일), 트랜잭션 짧게.

### Q4. 낙관적 잠금(Optimistic Locking)과 비관적 잠금(Pessimistic)의 차이는?
A. 비관적: 미리 락(SELECT FOR UPDATE). 충돌 많을 때 유리, 대기/교착 위험. 낙관적: 버전 체크 후 커밋(CAS). 충돌 적을 때 유리, 충돌 시 재시도. 읽기 많고 충돌 적으면 낙관적 권장.

### Q5. 2PC(Two-Phase Commit)의 문제점은?
A. 코디네이터 장애 시 블로킹(참여자 락 유지), 네트워크 파티션에 취약, 지연↑. 단일 데이터센터/짧은 트랜잭션에만 적합. 서비스 간에는 SAGA+Outbox+멱등성 권장.

### Q6. SAGA 패턴은 어떻게 구현하나요?
A. 긴 트랜잭션을 로컬 트랜잭션 시퀀스로 분해. 실패 시 보상 트랜잭션(Compensating)으로 롤백 효과. Choreography(이벤트 기반) 또는 Orchestration(중앙 조정자). 멱등성+재시도 필수.

### Q7. 멱등키(Idempotency Key)는 어떻게 설계하나요?
A. 클라이언트가 생성(UUID 또는 요청 내용 해시), 서버가 저장(TTL 24~48시간). 중복 요청 시 저장된 응답 반환. 저장소: Redis(빠름, 휘발), RDB(영속). 키 충돌 정책 명확히.

### Q8. 교착(Deadlock)은 어떻게 방지/탐지하나요?
A. 방지: 락 순서 통일(예: 항상 ID 오름차순), 타임아웃 설정, 트랜잭션 짧게. 탐지: DB의 교착 탐지기(wait-for 그래프), 한 쪽 abort 후 재시도. 교착률 모니터링(SLI).
