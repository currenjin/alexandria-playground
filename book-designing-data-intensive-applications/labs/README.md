# DDIA Hands-on Lab

DDIA를 "읽기"가 아니라 작은 시스템 구현으로 이해하기 위한 실습 트랙입니다.

핵심 원칙:

```text
Spring/Kotlin + TDD로
작은 저장소/트랜잭션/복제/샤딩/락/스트림 시스템을 만들고,
일부러 깨뜨린 뒤,
DDIA 개념으로 설명한다.
```

## 진행 규칙

1. 테스트를 먼저 작성한다. 실패를 본 뒤 구현한다.
2. 잘 동작하는 시스템보다, 깨지는 상황을 재현하는 것을 우선한다.
3. 각 실습마다 `무엇이 깨졌는지`, `왜 깨졌는지`, `어떤 DDIA 개념으로 설명되는지`를 남긴다.
4. 처음부터 Kafka, Cassandra, CockroachDB 같은 완성품을 쓰지 않는다. 작은 장난감 구현으로 원리를 먼저 잡는다.

## 권장 기술 스택

```text
언어: Kotlin 우선, Java 가능
프레임워크: Spring Boot는 HTTP/API가 필요할 때만 사용
테스트: JUnit5, AssertJ, Testcontainers
실행: Gradle, Docker Compose
저장소: 파일, SQLite/PostgreSQL
관찰: 로그, 간단한 CLI, 테스트 출력
```

## 추천 순서

책 순서보다 손에 잡히는 문제부터 시작합니다.

1. [파일 기반 Key-Value Store](./01-storage-engine-kv.md) - Ch.3
2. [계좌 이체와 트랜잭션 이상 현상](./02-transactions-account-transfer.md) - Ch.7
3. [Primary-Replica 복제](./03-replication-primary-replica.md) - Ch.5
4. [Shard 3개짜리 KV Store](./04-partitioning-sharded-kv.md) - Ch.6
5. [분산 시스템 장애 주입](./05-distributed-failure-injection.md) - Ch.8
6. [Leader Election과 Lock Server](./06-consistency-consensus-lock.md) - Ch.9
7. [스키마 진화와 호환성](./07-encoding-evolution.md) - Ch.4
8. [로그 파일 배치 처리](./08-batch-processing.md) - Ch.10
9. [주문 이벤트 스트림 처리](./09-stream-processing.md) - Ch.11
10. [CQRS + Event Sourcing](./10-cqrs-event-sourcing.md) - Ch.12

Ch.1~2는 중간중간 개념 정리와 설계 판단의 언어로 보강합니다.

## 각 실습 결과물

각 실습 폴더 또는 PR에는 아래를 남깁니다.

```text
README.md          # 무엇을 재현하는 실습인지
src/               # 구현 코드
test/              # 실패 재현 테스트 + 개선 검증 테스트
notes.md           # 깨진 상황, 원인, DDIA 연결
```

## 첫 시작

가장 먼저 할 실습은 `01-storage-engine-kv`입니다.

이유:

- 파일 I/O만으로 시작할 수 있다.
- 인덱스, 로그, compaction, tombstone을 직접 만질 수 있다.
- 이후 복제/샤딩/트랜잭션 실습의 기반 저장소로 재사용할 수 있다.
