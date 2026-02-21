# DDIA 플레이북 리허설 가이드

운영 플레이북(`runbook.md`)이 실제 장애 상황에서도 작동하는지 월 단위로 검증하기 위한 문서입니다.

## 운영 원칙
- 리허설은 프로덕션이 아닌 스테이징/샌드박스 환경에서 수행한다.
- 리허설 중 생성된 임시 설정(트래픽 제한, 강제 라우팅)은 종료 전에 반드시 원복한다.
- 각 시나리오는 `성공 기준`을 만족해야 통과로 판정한다.

## 월간 리허설 캘린더
| 주차 | 시나리오 | 연결 플레이북 |
|---|---|---|
| 1주차 | Replication Lag 급증 | `runbook.md#replication-lag` |
| 2주차 | Timeout/Retry Storm | `runbook.md#retry-storm` |
| 3주차 | Stream Consumer Lag 급증 | `runbook.md#stream-lag` |
| 4주차 | CDC 중단/스키마 비호환 | `runbook.md#cdc-schema` |

## 시나리오 템플릿
### 1) 준비
- 리허설 책임자(Incident Commander), 실행자(Operator), 기록자(Recorder) 지정
- 기준 메트릭 스냅샷 저장(시작 전 10분 평균)

### 2) 장애 주입
- 정의된 강도로 장애를 주입한다.
- 주입 시각과 강도(오류율/지연/중단 시간)를 기록한다.

### 3) 대응 실행
- `runbook.md` 절차대로 `즉시 조치 -> 복구`를 수행한다.
- 의사결정과 실행 시각을 타임라인으로 남긴다.

### 4) 통과 기준 검증
- 아래 공통 기준을 만족하면 통과로 판정한다.
  - `T_detect`(탐지 시간) <= 5분
  - `T_mitigate`(완화 시작) <= 10분
  - `T_recover`(정상화) <= 30분
  - 복구 후 핵심 SLI 10분 내 기준치 회복

### 5) 종료/원복
- 임시 라우팅, 차단 정책, 리소스 증설을 원복한다.
- 24시간 내 사후 회고(Action Item 포함) 작성

## 시나리오별 성공 기준
### Replication Lag 급증
- 성공 기준
  - 리더 라우팅 전환 후 `read_your_writes.violation_rate`가 0에 수렴
  - 복구 후 `replication_lag_ms.p95`가 기준치로 회복

### Timeout/Retry Storm
- 성공 기준
  - Retry Budget 적용 후 `dep.retry_rate`가 예산 이내로 안정화
  - `dep.timeout_rate`가 15분 내 하락 추세 전환

### Stream Consumer Lag 급증
- 성공 기준
  - `stream.lag_ms.p95`가 30분 내 기준치 복귀
  - 체크포인트 실패 없이 처리량 회복

### CDC 중단/스키마 비호환
- 성공 기준
  - 재기동/재배포 후 `cdc.lag_ms.p95`가 감소 추세
  - 감사 배치 결과 `audit.violation_count = 0`

## 리허설 결과 기록 템플릿
```md
### Drill: <scenario-name>
- Date:
- Commander / Operator / Recorder:
- Injection:
- T_detect:
- T_mitigate:
- T_recover:
- Metric snapshots (before/after):
- What worked:
- What failed:
- Action items (owner, due date):
```
