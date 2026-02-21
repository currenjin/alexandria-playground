# Ch05 Replication

## TL;DR (3문장)
- 복제는 **가용성, 지연 시간, 읽기 처리량**을 높이기 위함이다. 단일 리더, 다중 리더, 리더리스 중 선택.
- **동기 vs 비동기** 복제가 **일관성 vs 가용성** 트레이드오프를 결정한다. 동기는 강한 일관성, 비동기는 데이터 손실 가능.
- **복제 지연**으로 인한 이상(stale read, 인과 역전)을 이해하고, **Read-your-writes, 모노토닉 읽기** 등 보장 수준을 설계한다.

## Key Ideas
- **단일 리더(Leader-based)**
  - 쓰기는 리더만, 읽기는 리더/팔로워
  - 장애 시 팔로워 승격(failover). 데이터 손실/스플릿 브레인 위험
- **다중 리더(Multi-leader)**
  - 지리 분산, 오프라인 클라이언트에 유리
  - **쓰기 충돌** 처리 필요(LWW, 병합, CRDT)
- **리더리스(Leaderless)**
  - 정족수(Quorum) 기반. W+R>N으로 최신성 확보
  - **sloppy quorum/hinted handoff**로 가용성↑, 일관성↓
- **동기 복제**: 리더가 팔로워 확인 후 응답. 내구성↑, 지연↑, 가용성↓
- **비동기 복제**: 리더 즉시 응답. 지연↓, 데이터 손실 가능(failover 시)
- **복제 지연 이상**
  - **Read-your-writes**: 내가 쓴 것을 즉시 못 읽음
  - **모노토닉 읽기**: 과거로 회귀(시간 역전)
  - **인과 일관성**: 원인-결과 순서 역전
- **충돌 해결 전략**
  - LWW(Last Write Wins): 단순, 데이터 손실 가능
  - 병합(Union, Custom logic): 복잡
  - CRDT: 자동 병합 가능 자료구조

## Trade-offs
| 선택 | 장점 | 단점 | 언제 |
|---|---|---|---|
| **단일 리더** | 단순, 일관성 보장 쉬움 | 쓰기 병목, 리더 장애 시 전환 비용 | 대부분의 OLTP |
| **다중 리더** | 지리 분산, 오프라인 지원 | 충돌 해결 복잡, 일관성 보장 어려움 | 글로벌 서비스, 협업 앱 |
| **리더리스** | 가용성↑, 단일 장애점 없음 | 정족수 관리, 충돌 해결, 읽기 복잡 | 높은 가용성 요구, AP 시스템 |
| **동기 복제** | 강한 내구성, 데이터 손실 없음 | 지연↑, 팔로워 장애 시 쓰기 차단 | 금융, 결제 등 데이터 손실 불가 |
| **비동기 복제** | 지연↓, 가용성↑ | failover 시 데이터 손실 가능 | 대부분의 읽기 복제본 |
| **반동기(Semi-sync)** | 최소 1개 팔로워 보장 | 설정 복잡, 1개 장애까지만 안전 | 내구성과 가용성 균형 |
| **LWW 충돌 해결** | 단순, 예측 가능 | 데이터 손실(동시 쓰기) | 멱등 작업, 최종 상태만 중요 |
| **CRDT** | 자동 병합, 충돌 없음 | 지원 자료구조 제한, 복잡 | 실시간 협업, 카운터, 집합 |

## Apply to Our Domain (Orders/Dispatch)
- **주문 DB**: 단일 리더(MySQL/PostgreSQL) + 비동기 읽기 복제본. 읽기 분산, failover 자동화.
- **Read-your-writes 보장**: 쓰기 직후 조회는 리더 라우팅 또는 세션 스티키.
- **배차 카운터**: 정족수 기반(N=3, W=2, R=2) 또는 단일 리더 + 동기 복제 1개(반동기).
- **지리 분산(향후)**: 다중 리더 검토. 주문 생성은 LWW 불가 → CRDT 카운터 또는 중앙 조정자.
- **복제 지연 모니터링**: `replication_lag_ms` 추적, 임계치 초과 시 읽기 경로 리더 전환.

## Metrics & SLO (30일 롤링)
### SLI
- `replication_lag_ms.p95`: 리더-팔로워 지연
- `failover.duration_ms`: 리더 장애 시 전환 시간
- `failover.data_loss_events`: failover 시 데이터 손실 발생 수
- `read_your_writes.violation_rate`: 쓰기 후 읽기 불일치 비율
- `conflict.resolution_rate`: 다중 리더 충돌 발생/해결 비율

### SLO
- `replication_lag_ms.p95 < 1000`
- `failover.duration_ms < 30000`
- `failover.data_loss_events = 0` (동기/반동기 경로)
- `read_your_writes.violation_rate < 0.1%`

## Open Questions
- 현재 복제 지연이 **Read-your-writes 위반**을 얼마나 유발하는가? (측정 필요)
- failover 자동화 시 **스플릿 브레인** 방지 메커니즘(펜싱, STONITH)은 충분한가?
- 다중 리전 확장 시 **다중 리더 vs 리더리스** 중 어떤 모델이 주문 도메인에 적합한가?
- 비동기 복제에서 **데이터 손실 허용 범위**(RPO)는 어떻게 정의할 것인가?

## Hands-on
### 실험 목표
- 비동기 복제에서 발생하는 stale read를 재현하고 완화 전략(리더 라우팅)의 효과를 비교한다.

### 준비
- 리더 1, 팔로워 1 이상인 로컬/스테이징 DB
- 읽기 트래픽을 리더/팔로워로 분리할 수 있는 라우팅 설정

### 실행 단계
1. 동일 사용자에 대해 `write -> immediate read`를 1,000회 반복한다.
2. 읽기 대상을 팔로워로 고정해 위반율(`read-your-writes`)을 측정한다.
3. 동일 테스트를 "쓰기 후 3초 리더 라우팅" 정책으로 재실행한다.
4. failover를 1회 유도하고 손실 이벤트/전환 시간을 기록한다.

### 검증 메트릭
- `read_your_writes.violation_rate`
- `replication_lag_ms.p95`
- `failover.duration_ms`

### 실패 시 체크포인트
- 팔로워 지연이 급증하면 읽기 경로를 즉시 리더로 전환
- failover 이후 이중 리더 징후(동시 쓰기 허용) 로그 확인

## Incident Playbook
- [Replication Lag 급증](../runbook.md#replication-lag)
