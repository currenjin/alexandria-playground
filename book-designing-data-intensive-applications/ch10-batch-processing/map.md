# Ch10 Batch Processing

## TL;DR (3문장)
- 배치 처리는 **대량 데이터를 한 번에** 처리한다. 입력 불변, 출력 재생성 가능 → 멱등성/재시도 용이.
- **MapReduce**는 분산 배치의 기본 모델. Map(추출/변환) → Shuffle(그룹핑) → Reduce(집계).
- 현대 배치 엔진(Spark, Flink Batch)은 **DAG 실행, 메모리 캐싱**으로 MapReduce 한계 극복.

## Key Ideas
- **Unix 철학과 배치**
  - 단일 목적 도구, 파이프라인 조합
  - 입력/출력 불변 → 디버깅, 재실행 용이
- **MapReduce 모델**
  - Map: 입력 레코드 → (key, value) 쌍 방출
  - Shuffle: 같은 키를 같은 Reducer로 파티셔닝
  - Reduce: 키별 값 집계
- **분산 파일 시스템(HDFS)**
  - 데이터 지역성: 연산을 데이터가 있는 노드로 이동
  - 복제로 내결함성
- **조인 전략**
  - Sort-Merge Join: 큰 데이터셋 간 조인
  - Broadcast Join: 작은 테이블을 모든 노드에 복제
  - Partitioned Join: 같은 키로 파티셔닝된 데이터 간 조인
- **MapReduce 한계**
  - 중간 결과를 디스크에 기록(I/O 오버헤드)
  - 반복 연산에 비효율(ML 등)
- **현대 배치 엔진(Spark, Flink)**
  - DAG 기반 실행 계획
  - 메모리 캐싱(RDD, Dataset)
  - 지연 실행, 최적화
- **출력 무결성**
  - 배치 출력은 원자적(성공 또는 전체 실패)
  - 재시도 시 동일 출력 보장(멱등)

## Trade-offs
| 선택 | 장점 | 단점 | 언제 |
|---|---|---|---|
| **MapReduce** | 단순, 내결함성↑, 대용량 | 느림(디스크 I/O), 반복 비효율 | 레거시, 단순 ETL |
| **Spark** | 메모리 캐싱, 빠른 반복 | 메모리 요구↑, 복잡 | ML, 반복 처리, 대화형 분석 |
| **Sort-Merge Join** | 대용량 조인 가능 | 정렬 비용, I/O↑ | 큰 테이블 간 조인 |
| **Broadcast Join** | 작은 테이블 조인 빠름 | 메모리 한계, 네트워크↑ | 차원 테이블 조인 |
| **일일 배치** | 단순, 예측 가능 | 지연↑(최대 24시간) | 리포트, 정산 |
| **마이크로 배치** | 지연↓(분 단위) | 오버헤드↑, 복잡 | 준실시간 대시보드 |

## Apply to Our Domain (Orders/Dispatch)
- **일일 정산 배치**
  - 입력: 주문 이벤트 로그(HDFS/S3)
  - 처리: Spark로 일별 매출, 수수료, 정산금 집계
  - 출력: 정산 테이블(Parquet) → 결제 시스템 연동
- **주문 분석 ETL**
  - 원천: OLTP DB CDC → Kafka → S3
  - 변환: Spark로 비정규화, 차원 테이블 조인
  - 적재: 열 저장소(BigQuery, Redshift)
- **재처리/백필**
  - 입력 불변 → 로직 수정 후 전체 재실행
  - 출력 버저닝: `output/v2/date=2024-01-01/`
- **조인 전략**
  - 주문 + 사용자: Broadcast Join(사용자 테이블 작음)
  - 주문 + 상품: Partitioned Join(둘 다 대용량)

## Metrics & SLO (30일 롤링)
### SLI
- `batch.job.duration_min.p95`: 배치 작업 소요 시간
- `batch.job.success_rate`: 작업 성공률
- `batch.data.freshness_hours`: 출력 데이터 신선도(지연)
- `batch.reprocess.count`: 재처리 횟수

### SLO
- `batch.job.duration_min.p95 < 60` (1시간 이내)
- `batch.job.success_rate >= 99%`
- `batch.data.freshness_hours < 6` (6시간 이내 반영)
- `batch.reprocess.count < 2/week`

## Open Questions
- 현재 배치 파이프라인의 **병목 단계**(I/O, 셔플, 조인)는 어디인가?
- **마이크로 배치**(Spark Structured Streaming)로 전환 시 지연 개선 폭은?
- 재처리 시 **멱등성 보장**을 위한 출력 경로/버저닝 전략은?
- 배치 실패 시 **알람 및 자동 재시도** 정책은?

## Hands-on
### 실험 목표
- 조인 전략(Broadcast vs Partitioned)에 따른 처리 시간/비용 차이를 측정한다.

### 준비
- 대용량 사실 테이블 + 소형/중형 차원 테이블
- 동일 로직을 두 조인 전략으로 실행 가능한 잡 정의

### 실행 단계
1. Broadcast Join으로 배치를 실행해 소요시간/메모리 사용량을 기록한다.
2. Partitioned Join으로 동일 작업을 실행한다.
3. 입력량을 2배로 늘려 두 전략의 확장 추세를 비교한다.
4. 실패 레코드 주입 후 재실행 시 멱등 결과 여부를 확인한다.

### 검증 메트릭
- `batch.job.duration_min`
- `batch.shuffle_bytes`
- `executor.memory_peak_mb`
- `batch.result.diff_count`

### 실패 시 체크포인트
- OOM 발생 시 Broadcast 임계값 축소 또는 파티션 전략 전환
- diff_count > 0 이면 출력 경로 원자성/중복 제거 로직 점검
