# Ch07 Transaction
## TL;DR (3문장)

* 트랜잭션은 동시성에서 **불변식(invariant)** 을 지키기 위한 도구다. 격리 수준 선택이 **허용/차단**할 이상(anomaly)을 결정한다.
* **Snapshot Isolation(RR)** 은 dirty/non-repeatable/read skew는 막지만 **write skew/phantom**은 막지 못한다. **Serializable**이 가장 안전하나 비용↑.
* 서비스 간에는 2PC보다 **SAGA + Outbox + 멱등성**으로 일관성을 설계하고, **재시도/백오프/서킷**을 전제한다.

## Key Ideas

* **ACID 재정의(실무 관점)**: A(원자성)·I(격리)·D(내구성)는 스토리지+CC로 보장, **C(일관성)**은 애플리케이션 불변식으로 정의해야 함.
* **격리 수준과 이상**
    * RC(Read Committed): dirty read x, 그 외 대부분 허용
    * RR/SI(Repeatable Read / Snapshot Isolation): dirty/non-repeat/read skew x, **write skew o**, phantom (DB 의존)
    * Serializable: 주요 이상 전부 x (가장 안전)
* **주요 이상**

    * **Dirty Read**: 커밋 전 데이터 읽음
    * **Non-repeatable Read**: 같은 행 재조회 값 변동
    * **Read Skew**: 서로 다른 행/테이블 사이 시간 왜곡
    * **Write Skew**: 두 트랜잭션이 각각 검사-쓰기로 **불변식 위반**
    * **Lost Update**: 동시 갱신 중 덮어쓰기
    * **Phantom**: 조건에 맞는 행 집합이 동시성으로 바뀜
* **동시성 제어(요약)**

    * **2PL(락 기반)**: 행/범위(갭/프레디킷) 락으로 직렬화 보장, 교착/대기↑
    * **MVCC + SI**: 스냅샷 읽기(락 적음), 대신 **write-write 충돌/쓰기 스큐** 가능
    * **SSI(Serializable SI)**: SI 위에 충돌 검사로 직렬화 보장(충돌 시 abort)
    * **명시적 원자 연산/제약**: `UPSERT/UNIQUE/FOREIGN KEY`, `SELECT ... FOR UPDATE`
* **분산 트랜잭션**

    * 2PC는 블로킹/파티션 장애에 취약. 실무에선 **SAGA(보상 트랜잭션)** + **Outbox/CDC** + **소비자 멱등성**을 조합
* **패턴**

    * 검사-후-쓰기(Check-then-write)는 SI에서 위험 → **원자적 카운터/제약/락/큐**로 대체
    * 장시간 트랜잭션은 핫락/재시도 폭증 유발 → **작게 쪼개기/오프로드**

## Trade-offs

| 선택                        | 장점             | 단점                        | 언제               |
|---------------------------|----------------|---------------------------|------------------|
| **RC**                    | 지연·경합↓, 처리량↑   | 읽기 왜곡/스큐 허용               | 단순 조회/보고용        |
| **RR/SI**                 | 읽기 안정성↑, 락 경합↓ | **Write Skew/Phantom** 위험 | 대부분의 OLTP 기본값    |
| **Serializable(2PL/SSI)** | 불변식 강보장        | 지연/Abort/교착↑              | 금전·재고 등 강한 제약    |
| **SELECT ... FOR UPDATE** | 잃어버린 업데이트 방지   | 락 경합/대기↑                  | 소수 행 갱신 충돌 방지    |
| **UNIQUE/제약/원자연산**        | 애플리케이션 버그 차단   | 스키마 의존, 유연성↓              | 불변식은 **제약으로** 표현 |
| **2PC**                   | 전역 원자성         | 복잡/장애취약/락홀드               | 내부 단일 스토리지 계열    |
| **SAGA+Outbox**           | 탄력/장애내성, 독립 배포 | 보상 로직 필요(복잡)              | 서비스 간 업무 흐름      |

## Apply to Our Domain (주문/배차)

* **불변식 1: "동시 배차 ≤ 10건/지역"**

    * 옵션A: **Serializable** + 프레디킷/갭 락(성능 비용↑)
    * 옵션B: **카운터 테이블**에 `atomic increment` 후 임계 초과 시 롤백(권장)
    * 옵션C: **큐**(단일 워커)로 직렬화
* **불변식 2: 주문번호 유일** → DB **UNIQUE** + **Idempotency-Key** (재시도 안전)
* **Lost Update 방지**: 버전 칼럼으로 **CAS(낙관적 잠금)** 또는 `FOR UPDATE`
* **서비스 간 일관성**: OrderCreated → **Outbox/CDC**로 발행, 소비자는 **멱등 처리**
* **읽기-당장-쓰기(Read-your-writes)**: 사용자 세션은 **리더 라우팅/세션 스티키**

## Metrics & SLO (권장)

* `db.tx.abort_rate` (전체/사유별: deadlock, serialization, lock timeout)
* `db.lock_wait_ms.p95` / `db.row_version_conflicts`
* `api.idempotency.dedup_hits` (중복 차단 성공률)
* **SLO 예시(30일)**: `deadlock_rate < 0.1%`, `serialization_fail_rate < 0.5%`, `lock_wait_p95 < 50ms`

## Open Questions

* 어떤 경로에 **Serializable**을 적용/예외로 둘 것인가? (화이트리스트)
* 카운터 테이블 방식과 큐 직렬화의 **성능/가용성** 비교 실험 계획은?
* 멱등키 **보존 TTL**과 저장소(캐시/RDB) 선택은?
* 다중 리전에선 **세션 일관성**을 어떻게 보장할 것인가?(리더 고정/CRDT/지연 허용)

## Hands-on
### 실험 목표
- `write skew`를 재현하고 방지 전략(Serializable, 원자 카운터)의 비용 차이를 측정한다.

### 준비
- 동시성 테스트 스크립트(동일 불변식에 동시 요청)
- 격리 수준 변경 가능한 DB 환경

### 실행 단계
1. RR/SI에서 동일 불변식 경로에 동시 요청을 500회 주입한다.
2. 위반 케이스(예: 동시 배차 제한 초과) 발생률을 측정한다.
3. 같은 시나리오를 Serializable로 재실행한다.
4. 마지막으로 카운터 테이블 원자 증가 방식으로 재실행한다.

### 검증 메트릭
- `invariant.violation_rate`
- `db.tx.abort_rate`
- `db.lock_wait_ms.p95`
- `api.latency_ms.p95`

### 실패 시 체크포인트
- 교착 급증 시 트랜잭션 범위/락 순서 재검토
- abort율이 SLO 초과면 Serializable 적용 범위를 축소해 핵심 경로만 보호
