# Designing Data-Intensive Applications 스터디 노트

> Martin Kleppmann의 "Designing Data-Intensive Applications"를 실무 관점에서 정리한 노트입니다.
> 각 챕터별 핵심 개념, 트레이드오프, 도메인 적용 사례(주문/배차)를 다룹니다.

## Part I. Foundations of Data Systems

### [Ch01. Reliability, Scalability, Maintainability](./ch01-reliability-scalability-maintainability/map.md)
- **핵심**: 품질 = 신뢰성 + 확장성 + 유지보수성. 평균 대신 p95/p99로 측정.
- **키워드**: SLO/SLI, Tail Latency, 결함 vs 장애, 수평/수직 확장

### [Ch02. Data Models and Query Languages](./ch02-data-models-and-query-languages/map.md)
- **핵심**: 스키마는 접근 패턴이 정한다. 인덱스는 읽기↑, 쓰기↓.
- **키워드**: 관계형 vs 문서 모델, 정규화 vs 비정규화, 커버링 인덱스

### [Ch03. Storage and Retrieval](./ch03-storage-and-retrieval/map.md)
- **핵심**: LSM-Tree(쓰기 최적화) vs B-Tree(읽기 최적화). OLTP vs OLAP 분리.
- **키워드**: Write Amplification, Compaction, 클러스터드 인덱스, 열 저장소

### [Ch04. Encoding and Evolution](./ch04-encoding-and-evolution/map.md)
- **핵심**: 스키마 진화는 필수. 전방/후방 호환성으로 무중단 배포.
- **키워드**: JSON vs Protobuf vs Avro, 스키마 레지스트리, 롤링 배포

## Part II. Distributed Data

### [Ch05. Replication](./ch05-replication/map.md)
- **핵심**: 복제 = 가용성 + 지연 + 처리량. 동기 vs 비동기가 일관성↔가용성 결정.
- **키워드**: 단일/다중 리더, 리더리스, 복제 지연, Read-your-writes

### [Ch06. Partitioning](./ch06-partitioning/map.md)
- **핵심**: 파티셔닝 = 처리량 + 저장 확장. 핫스팟 방지가 핵심.
- **키워드**: 키 범위 vs 해시 파티셔닝, Consistent Hashing, 리밸런싱

### [Ch07. Transactions](./ch07-transaction/map.md)
- **핵심**: 트랜잭션 = 불변식 보호. 격리 수준이 허용할 이상(anomaly) 결정.
- **키워드**: ACID, 격리 수준, Write Skew, 2PC vs SAGA, 멱등성

### [Ch08. The Trouble with Distributed Systems](./ch08-the-trouble-with-distributed-systems/map.md)
- **핵심**: 네트워크는 손실/지연/중복 가능. 시계는 신뢰 불가. 타임아웃+재시도+멱등성.
- **키워드**: Failure Detector, 헤지드 요청, 펜싱 토큰, Retry Budget

### [Ch09. Consistency and Consensus](./ch09-consistency-and-consensus/map.md)
- **핵심**: 선형성은 강하지만 비용↑. 합의는 리더 선출/분산 락의 기반.
- **키워드**: CAP 정리, Raft/Paxos, ZooKeeper, 전체 순서 브로드캐스트

## Part III. Derived Data

### [Ch10. Batch Processing](./ch10-batch-processing/map.md)
- **핵심**: 대량 데이터 일괄 처리. 입력 불변 → 멱등성/재처리 용이.
- **키워드**: MapReduce, Spark, 조인 전략, 데이터 스큐

### [Ch11. Stream Processing](./ch11-stream-processing/map.md)
- **핵심**: 이벤트 즉시 처리. 이벤트 시간 vs 처리 시간 구분 필수.
- **키워드**: 워터마크, 윈도우, Exactly-once, 백프레셔

### [Ch12. The Future of Data Systems](./ch12-the-future-of-data-systems/map.md)
- **핵심**: 다중 저장소 조합. End-to-End 정확성은 멱등성+제약+감사로.
- **키워드**: CDC, 이벤트 소싱, CQRS, Lambda/Kappa, 진실의 원천

---

## Quick Reference

### 일관성 모델 스펙트럼
```
선형성(Linearizability) > 순차 일관성 > 인과 일관성 > 최종 일관성
   (가장 강함, 비용↑)                              (가장 약함, 가용성↑)
```

### 격리 수준과 방지하는 이상
| 격리 수준 | Dirty Read | Non-repeatable | Read Skew | Write Skew | Phantom |
|----------|------------|----------------|-----------|------------|---------|
| Read Committed | X | O | O | O | O |
| Repeatable Read/SI | X | X | X | O | O/X |
| Serializable | X | X | X | X | X |

### 복제 모델 비교
| 모델 | 쓰기 | 일관성 | 가용성 | 복잡도 |
|-----|-----|-------|-------|-------|
| 단일 리더 | 리더만 | 강함 | 리더 의존 | 낮음 |
| 다중 리더 | 여러 리더 | 충돌 해결 필요 | 높음 | 높음 |
| 리더리스 | 모든 노드 | 정족수 의존 | 높음 | 높음 |

### 데이터 처리 패러다임
| 패러다임 | 지연 | 처리량 | 재처리 | 복잡도 |
|---------|-----|-------|-------|-------|
| 요청-응답 | 낮음 | 중간 | 어려움 | 낮음 |
| 배치 | 높음(시간~일) | 높음 | 쉬움 | 중간 |
| 스트림 | 낮음(초~분) | 높음 | 가능 | 높음 |

## 보강 자료
- [DDIA 핵심 용어집](./glossary.md)
- [DDIA 학습 로드맵 (실무 적용형)](./study-roadmap.md)

---

## 파일 구조
```
ch00-example/           # 예제 템플릿
  ├── map.md           # 개념 맵 (TL;DR, Key Ideas, Trade-offs, 도메인 적용)
  ├── qa.md            # Q&A
  ├── atoms/           # 원자적 개념 정리
  ├── adr/             # Architecture Decision Records
  └── diagrams/        # 다이어그램

chXX-{chapter-name}/
  ├── map.md           # 챕터별 개념 맵
  └── qa.md            # 챕터별 Q&A
```
