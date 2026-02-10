# Query Execution Plan

MySQL의 EXPLAIN을 직접 실행하고, 쿼리를 최적화하는 과정을 체험하는 학습 프로젝트.

## 실행 방법

```bash
# MySQL 컨테이너 실행
docker-compose up -d

# 전체 테스트 실행
./gradlew test

# 개별 시나리오 실행
./gradlew test --tests "com.currenjin.explain.FullTableScanToIndexScanTest" --info

# MySQL 직접 접속
docker exec -it query-execution-plan-mysql mysql -uexplain -pexplain explain_lab
```

## 학습 시나리오

### Scenario 1: Full Table Scan -> Index Scan

> [`FullTableScanToIndexScanTest.java`](src/test/java/com/currenjin/explain/FullTableScanToIndexScanTest.java)

인덱스의 가장 기본적인 효과를 체험한다.

| 구분 | 쿼리 | EXPLAIN type | rows |
|------|------|-------------|------|
| Before | `SELECT * FROM member WHERE email = ?` (인덱스 없음) | ALL | 10,000+ |
| After | 동일 쿼리 + `CREATE INDEX idx_member_email ON member(email)` | ref | 1 |

---

### Scenario 2: Composite Index Column Order

> [`CompositeIndexColumnOrderTest.java`](src/test/java/com/currenjin/explain/CompositeIndexColumnOrderTest.java)

복합 인덱스에서 컬럼 순서가 성능에 미치는 영향을 체험한다.
핵심 원칙: **등치(=) 조건 컬럼을 먼저, 범위(>) 조건 컬럼을 나중에**.

| 구분 | 인덱스 | 쿼리 | 결과 |
|------|--------|------|------|
| 인덱스 없음 | - | `WHERE status = ? AND order_date > ?` | ALL (전체 스캔) |
| 잘못된 순서 | `(order_date, status)` | 동일 | 범위 조건이 먼저라 비효율적 |
| 올바른 순서 | `(status, order_date)` | 동일 | range (스캔 행 수 감소) |

---

### Scenario 3: N+1 Problem -> JOIN

> [`NplusOneToJoinTest.java`](src/test/java/com/currenjin/explain/NplusOneToJoinTest.java)

N+1 문제와 JOIN의 차이를 실행 계획 관점에서 체험한다.

| 구분 | 패턴 | 쿼리 수 | EXPLAIN |
|------|------|---------|---------|
| N+1 | 주문 목록 조회 + 각 주문의 회원 개별 조회 | 101번 | orders: ALL, member: const (x100) |
| JOIN | `SELECT o.*, m.name FROM orders o JOIN member m ON ...` | 1번 | orders: ALL, member: eq_ref |

---

### Scenario 4: LIKE Pattern Optimization

> [`LikePatternOptimizationTest.java`](src/test/java/com/currenjin/explain/LikePatternOptimizationTest.java)

LIKE 검색에서 와일드카드 위치에 따른 인덱스 사용 여부를 체험한다.

| 패턴 | 인덱스 사용 | EXPLAIN type | 이유 |
|------|-----------|-------------|------|
| `LIKE '%Laptop%'` | X | ALL | 앞에 와일드카드가 있으면 인덱스 B-Tree 탐색 불가 |
| `LIKE 'Laptop%'` | O | range | 접두사 검색은 인덱스의 정렬 순서를 활용 가능 |

---

### Scenario 5: ORDER BY filesort Elimination

> [`OrderByFilesortEliminationTest.java`](src/test/java/com/currenjin/explain/OrderByFilesortEliminationTest.java)

정렬 쿼리에서 filesort가 발생하는 원인과 인덱스로 제거하는 방법을 체험한다.

| 구분 | 쿼리 | Extra | 의미 |
|------|------|-------|------|
| Before | `SELECT ... ORDER BY created_at DESC LIMIT 10` (인덱스 없음) | Using filesort | 전체 데이터를 메모리에서 정렬 |
| After | 동일 쿼리 + `CREATE INDEX ... ON product(created_at)` | (filesort 없음) | 인덱스가 이미 정렬되어 있으므로 상위 10건만 읽으면 됨 |

---

### Scenario 6: Covering Index

> [`CoveringIndexTest.java`](src/test/java/com/currenjin/explain/CoveringIndexTest.java)

커버링 인덱스로 테이블 룩업(I/O)을 제거하는 효과를 체험한다.

| 구분 | 인덱스 | Extra | 의미 |
|------|--------|-------|------|
| 단일 컬럼 인덱스 | `(status)` | (Using index 없음) | 인덱스에서 status를 찾은 후, name과 price를 가져오려면 테이블을 다시 읽어야 함 |
| 커버링 인덱스 | `(status, name, price)` | Using index | SELECT하는 모든 컬럼이 인덱스에 포함되어 테이블 룩업 불필요 |

## EXPLAIN type 정리

성능이 좋은 순서:

| type | 의미 |
|------|------|
| const | PK/유니크 인덱스로 1건 조회 |
| eq_ref | JOIN에서 PK/유니크 인덱스로 1건 매칭 |
| ref | 비유니크 인덱스로 조회 |
| range | 인덱스 범위 스캔 (>, <, BETWEEN, LIKE 'prefix%') |
| index | 인덱스 전체 스캔 (테이블 전체 스캔보다는 낫지만 비효율) |
| ALL | 테이블 전체 스캔 (가장 느림) |

## Tech Stack

- Spring Boot 3.3.3 / Java 17
- MySQL 8.0 (Docker)
- Spring Data JPA + Lombok
- JUnit 5 + AssertJ
