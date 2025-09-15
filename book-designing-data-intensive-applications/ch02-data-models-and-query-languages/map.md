# Ch02 Data Models and Query Languages
## TL;DR
- 스키마는 접근 패턴이 정한다. (무엇을 얼마나 자주/어떻게 읽고 쓰는가)
- **색인(인덱스)** 는 읽기를 빠르게 하지만 쓰기/공간 비용이 든다. (복수 인덱스 유지=쓰기 증폭)
- **문서 모델은 읽기 스키마(schema-on-read)** 에 가깝고 지역성/집계 읽기에 유리, 관계형은 조인/트랜잭션에 유리.
- 정규화 vs 비정규화는 쓰기 일관성 vs 읽기 성능의 교환.

## Key Ideas
- Access-Pattern First: “최근 7일 유저별 주문 20개”, “주문 상세 빠르게” 같은 핵심 질의를 먼저 적고 스키마/인덱스 설계.
- 인덱스 기본기: 복합 인덱스(예: (user_id, created_at DESC)), 커버링 인덱스(SELECT 컬럼이 전부 인덱스에 있을 때), 선택도(cardinality) 체크.
- 정규화/비정규화 믹스: 핵심 엔터티는 정규화, API 응답 최적화용 읽기 모델은 비정규화/스냅샷 필드(ex: shipping_address_snapshot).
- 참조 vs 내장(embedding): 변경 잦고 공유되는 것은 ID 참조, 항상 같이 읽는 작은 하위 구조는 내장.
- Row-store vs Column-store: 온라인 트랜잭션(OLTP)은 보통 행 저장, 분석/리포트(OLAP)는 열 저장(+압축) 채택.
- Schema-on-write vs read: RDB는 쓰기 시 강한 스키마, 문서/데이터 레이크는 읽기 시 해석(유연하지만 쿼리 복잡도↑).
- 무중단 스키마 변경 패턴: Expand → Migrate → Contract(새 컬럼/인덱스 추가 → 백필/듀얼리드 → 구식 제거).

## Trade-offs
| 선택                  | 장점              | 단점            | 언제               |
|---------------------|-----------------|---------------|------------------|
| **문자열 내장 저장**       | 한 번에 읽기, 조인 없음  | 중복/부분 업데이트 비용 | 응답에 항상 필요한 작은 필드 |
| **ID 참조**           | 중복 없음, 변경 전파 쉬움 | 조인/추가 조회 필요   | 재사용/변경 잦은 공통 엔터티 |
| **정규화**             | 쓰기 일관성·중복 최소    | 읽기 시 조인 비용    | 강한 정합·중복 방지 우선   |
| **비정규화(스냅샷)**       | 읽기/캐시 지역성↑      | 쓰기 시 동기화 부담   | 응답 최적화·과거 상태 보존  |
| **보조 인덱스 추가**       | p95 읽기↓, 스캔↓    | 쓰기/공간↑, 유지비↑  | 조회가 훨씬 많을 때      |
| **Row-store**       | 단건 읽기/쓰기 빠름     | 대규모 집계 비효율    | OLTP 경로          |
| **Column-store**    | 대용량 집계 효율↑      | 단건 트랜잭션 느림    | 리포트/분석 경로        |
| **Schema-on-write** | 검증·일관성↑         | 스키마 진화 마찰     | 핵심 트랜잭션 데이터      |
| **Schema-on-read**  | 유연성↑            | 쿼리 복잡/품질 편차   | 레이크/로그/실험 데이터    |

## Apply our Domain

도메인: 주문(Order) 조회/상세, “nearbyBaseOrderId” 탐색

### 모델 제안

주문 RDB(정규화) + 읽기 모델(비정규화) 병행

RDB 테이블: orders(id, user_id, status, created_at, total_amount, ...), order_items(order_id, product_id, qty, price, ...)

읽기 최적화 필드(스냅샷): shipping_address_snapshot, user_name_snapshot

문서형 대안(선택): order 문서에 items 내장(최근 주문 타임라인 API 등 한 번에 내려줄 때)

### 인덱스/쿼리
- 최근 주문 목록: INDEX (user_id, created_at DESC) → WHERE user_id=? ORDER BY created_at DESC LIMIT 20
- 상태 필터: INDEX (status, created_at DESC)
- 유니크: UNIQUE(order_no)
- nearbyBaseOrderId: 키셋 페이징(offset 대신 created_at < :cursor_created_at OR (created_at = :cursor AND id < :cursor_id)), 복합 인덱스 (created_at DESC, id DESC)

### 마이그레이션(무중단) 예시
- Expand: 새 컬럼 shipping_address_snapshot nullable 추가, 새 인덱스 생성
- Migrate: 백필 배치 + API 듀얼 리드(없으면 조인, 있으면 스냅샷 사용)
- Contract: 충분한 기간 후 구식 조인 경로 제거, NOT NULL 전환/제약 강화

## SLI, SLO (초안)
### SLI
- `db.orders.query.p95_ms`: 주문 목록/상세 쿼리 p95
- `db.orders.index_hit_rate`: 해당 쿼리 인덱스 히트율
- `db.orders.rows_scanned`: 쿼리당 스캔 로우 수 p95
- `api.orders.stale_read_rate`: 신선도 임계(예: 5분) 초과 응답 비율

### SLO (rolling 30d)
- `db.orders.query.p95_ms` < 150
- `db.orders.index_hit_rate` ≥ 95%
- `db.orders.rows_scanned.p95` ≤ 5k
- `api.orders.stale_read_rate` ≤ 0.5%

## Open Questions
- nearbyBaseOrderId 탐색에서 키셋 페이징으로 완전히 전환 가능한가? (정렬 기준/커서 설계 확정)
- 스냅샷 필드의 신선도 임계(ex. 5분)를 넘는 경우, 언제/어떻게 갱신? (비동기 리프레시/쓰기 시 동기화)
- 문서형 대안을 도입한다면, 어떤 엔드포인트에서 조인 제거→응답 시간/비용 이득이 가장 큰가?
- 보조 인덱스 추가로 쓰기 QPS가 얼마나 감소하는가? (배치 쓰기/인덱스 리빌드 윈도우 필요?)

## 메모(마이그레이션 팁)
- 컬럼 추가 시 nullable + 기본값으로 시작 → 애플리케이션이 null 허용하도록 먼저 배포(네가 적어둔 포인트 아주 좋음!)
- 인덱스는 온라인 생성/백그라운드 옵션 사용, 리빌드 시간 동안 배포 금지 윈도우 설정
- 이중 쓰기가 필요하면 가능한 Outbox/이벤트로 일관성 보강, 아니면 듀얼 리드로 점진 전환