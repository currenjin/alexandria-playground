# DDIA 핵심 용어집

`Designing Data-Intensive Applications`를 읽을 때 자주 헷갈리는 용어를 빠르게 찾아보기 위한 요약입니다.

## Reliability / Scalability / Maintainability
- **Reliability(신뢰성)**: 장애가 나도 시스템이 기대 동작을 유지하는 능력.
- **Scalability(확장성)**: 부하 증가 시 자원 증설로 성능 목표를 유지하는 능력.
- **Maintainability(유지보수성)**: 기능 변경, 운영, 디버깅을 빠르게 수행할 수 있는 성질.
- **SLI/SLO**: 관측 지표(SLI)와 목표치(SLO). 운영 의사결정 기준.
- **Tail Latency(p95/p99)**: 평균이 아닌 상위 지연 구간. 사용자 체감에 직접 영향.

## Data Models / Storage
- **Normalization(정규화)**: 중복 최소화 중심 설계. 쓰기 무결성에 유리.
- **Denormalization(비정규화)**: 읽기 성능 중심 설계. 중복/동기화 비용 증가.
- **LSM-Tree**: 쓰기 최적화 저장 구조. 컴팩션 비용 관리가 핵심.
- **B-Tree**: 읽기/포인트 조회에 강한 범용 인덱스 구조.
- **Write Amplification**: 한 번의 논리적 쓰기가 물리적으로 여러 번 기록되는 현상.

## Encoding / Evolution
- **Backward Compatibility**: 새 소비자가 예전 데이터 포맷을 읽을 수 있음.
- **Forward Compatibility**: 예전 소비자가 새 데이터 포맷을 처리 가능.
- **Schema Registry**: 메시지 스키마 버전/호환성 규칙을 중앙 관리.
- **Idempotency(멱등성)**: 같은 요청을 여러 번 처리해도 결과가 동일한 성질.

## Replication / Partitioning
- **Leader-Follower Replication**: 리더에 쓰고 팔로워로 복제. 운영 단순, 리더 장애 대응 필요.
- **Multi-Leader Replication**: 여러 리더에서 쓰기 허용. 충돌 해결 전략 필수.
- **Leaderless Replication**: 정족수 기반 읽기/쓰기. 높은 가용성, 정합성 설계 복잡.
- **Replication Lag**: 원본 쓰기와 복제 반영 사이 시간 차이.
- **Partitioning(샤딩)**: 데이터를 여러 노드로 분할 저장해 처리량/용량 확장.
- **Hot Partition**: 특정 파티션으로 트래픽이 쏠려 병목이 생기는 상태.

## Transactions / Consistency
- **ACID**: 원자성/일관성/격리성/지속성 보장.
- **Isolation Level**: 동시성 상황에서 허용되는 이상(anomaly) 범위.
- **Write Skew**: 스냅샷 기반 격리에서 발생 가능한 불변식 위반 패턴.
- **Serializability**: 모든 트랜잭션이 직렬 실행과 동일 결과를 보장하는 가장 강한 격리.
- **Linearizability**: 단일 객체 연산이 실시간 순서를 따르는 강한 일관성 모델.
- **Consensus(합의)**: 장애가 있어도 노드들이 하나의 결정에 도달하는 문제.

## Batch / Stream / Derived Data
- **OLTP vs OLAP**: 트랜잭션 처리 중심 vs 분석 쿼리 중심 워크로드.
- **Data Warehouse**: 분석 질의를 위한 통합/열지향 저장소.
- **Event Time**: 이벤트가 실제 발생한 시간.
- **Processing Time**: 시스템이 이벤트를 처리한 시간.
- **Watermark**: 특정 시점 이전 이벤트가 대부분 도착했다고 가정하는 경계.
- **Exactly-once Effect**: 멱등성/체크포인트/트랜잭션 조합으로 중복 없는 효과를 달성.
- **CDC(Change Data Capture)**: 데이터베이스 변경 로그를 이벤트 스트림으로 추출.
- **Event Sourcing**: 현재 상태 대신 상태 변화를 이벤트로 저장하는 패턴.
- **CQRS**: 쓰기 모델과 읽기 모델을 분리해 각 경로를 최적화하는 구조.

## Architecture Patterns
- **Lambda Architecture**: 배치 + 스트림 이중 경로로 정확성과 저지연을 동시 확보.
- **Kappa Architecture**: 스트림 단일 경로, 재처리는 리플레이로 처리.
- **Source of Truth**: 특정 데이터 항목의 권위 있는 원천 저장소.
- **Derived Data**: 원천 데이터를 변환/집계해 만든 보조 모델(검색, 캐시, 리포트).
