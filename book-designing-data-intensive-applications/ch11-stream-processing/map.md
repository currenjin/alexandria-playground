# Ch11 Stream Processing

## TL;DR (3문장)
- 스트림 처리는 **이벤트가 발생하는 즉시** 처리한다. 배치보다 낮은 지연, 무한 데이터셋 처리.
- **이벤트 시간 vs 처리 시간**을 구분해야 한다. 워터마크로 지연 이벤트 처리, 윈도우로 시간 범위 집계.
- **Exactly-once**는 어렵다. 멱등성 + 트랜잭션 + 체크포인트 조합으로 효과적 exactly-once 달성.

## Key Ideas
- **메시지 전달 보장**
  - At-most-once: 메시지 유실 가능
  - At-least-once: 중복 가능 → 소비자 멱등성 필요
  - Exactly-once: 시스템 레벨 보장 어려움 → 효과적 exactly-once
- **이벤트 시간 vs 처리 시간**
  - 이벤트 시간: 이벤트 발생 시점
  - 처리 시간: 시스템 수신 시점
  - 지연 이벤트: 이벤트 시간 < 처리 시간 - 허용 지연
- **윈도우(Window)**
  - Tumbling: 고정 크기, 겹침 없음
  - Sliding: 고정 크기, 겹침 있음
  - Session: 활동 기반, 가변 크기
- **워터마크(Watermark)**
  - "이 시점 이전 이벤트는 모두 도착" 추정
  - 지연 이벤트 허용 창(allowed lateness)
- **상태 관리**
  - 로컬 상태 + 체크포인트(Flink, Kafka Streams)
  - 외부 상태 저장소(Redis 등) → 네트워크 오버헤드
- **스트림-테이블 이중성**
  - 스트림: 변경 로그(changelog)
  - 테이블: 스트림의 최신 스냅샷
  - CDC(Change Data Capture)로 DB → 스트림
- **조인**
  - Stream-Stream: 윈도우 조인
  - Stream-Table: 테이블 룩업(enrichment)
  - Table-Table: 양쪽 변경 시 재계산

## Trade-offs
| 선택 | 장점 | 단점 | 언제 |
|---|---|---|---|
| **At-least-once** | 단순, 유실 없음 | 중복 처리 필요 | 대부분의 스트림 처리 |
| **Exactly-once(시스템)** | 중복 없음 | 성능↓, 복잡, 경계 조건 | 금융, 결제 |
| **이벤트 시간 처리** | 정확한 시간 기반 분석 | 지연 이벤트 처리 복잡 | 분석, 집계 |
| **처리 시간 처리** | 단순, 지연↓ | 재처리 결과 불일치 | 모니터링, 알람 |
| **Tumbling 윈도우** | 단순, 예측 가능 | 경계 이벤트 분할 | 시간별/일별 집계 |
| **Session 윈도우** | 사용자 행동 분석에 적합 | 상태 관리 복잡 | 사용자 세션 분석 |
| **로컬 상태** | 빠름, 네트워크↓ | 복구 시 재구축 필요 | 대부분의 상태 저장 |
| **외부 상태(Redis)** | 공유 가능, 영속 | 네트워크 지연↑ | 서비스 간 공유 상태 |

## Apply to Our Domain (Orders/Dispatch)
- **실시간 주문 집계**
  - Kafka → Flink/Kafka Streams
  - Tumbling 윈도우(1분)로 지역별 주문 수 집계
  - 대시보드 실시간 업데이트
- **배차 이벤트 처리**
  - At-least-once + 멱등 소비자(주문ID 기준 중복 제거)
  - 이벤트 시간 기준 처리(배차 요청 시점)
  - 지연 이벤트 허용 창: 5분
- **CDC 기반 읽기 모델 동기화**
  - DB CDC(Debezium) → Kafka → 읽기 모델 갱신
  - Stream-Table 조인: 주문 이벤트 + 사용자 테이블 enrichment
- **알람/이상 탐지**
  - Sliding 윈도우(5분, 1분 슬라이드)로 에러율 계산
  - 임계치 초과 시 알람 발행

## Metrics & SLO (30일 롤링)
### SLI
- `stream.lag_ms.p95`: 소비자 지연(처리 시간 - 이벤트 시간)
- `stream.throughput_eps`: 초당 이벤트 처리량
- `stream.duplicate_rate`: 중복 이벤트 비율
- `stream.late_event_rate`: 지연 이벤트(워터마크 이후) 비율
- `checkpoint.duration_ms.p95`: 체크포인트 소요 시간

### SLO
- `stream.lag_ms.p95 < 5000` (5초 이내)
- `stream.duplicate_rate < 0.1%` (멱등 처리 후)
- `stream.late_event_rate < 1%`
- `checkpoint.duration_ms.p95 < 10000`

## Open Questions
- 현재 **소비자 지연**의 주요 원인은? (처리 병목, 파티션 불균형, 외부 호출)
- **워터마크 전략**을 어떻게 설정할 것인가? (주기적 vs 이벤트 기반)
- **지연 이벤트 허용 창**을 넘는 이벤트는 어떻게 처리할 것인가? (버림, 사이드 출력)
- Stream-Table 조인 시 **테이블 동기화 지연**이 결과에 미치는 영향은?
