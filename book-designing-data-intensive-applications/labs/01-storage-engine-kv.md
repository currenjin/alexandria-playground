# Lab 01. 파일 기반 Key-Value Store

연결 챕터: Ch.3 Storage and Retrieval

## 목표

파일 하나에서 시작해 append-only log, hash index, compaction, segment file로 업그레이드한다.

완성품 DB를 만드는 것이 아니라, 저장소 엔진이 왜 이런 구조를 갖는지 몸으로 확인한다.

## API

```kotlin
interface KeyValueStore {
    fun put(key: String, value: String)
    fun get(key: String): String?
    fun delete(key: String)
    fun compact()
}
```

## TDD 순서

1. `put` 후 `get` 하면 값이 나온다.
2. 없는 key는 `null`이다.
3. 같은 key를 두 번 `put`하면 마지막 값이 나온다.
4. `delete` 후 `get`하면 `null`이다.
5. 프로세스를 재시작해도 데이터가 남아 있다.
6. `compact` 후에도 최신 값은 유지된다.
7. 삭제된 key는 `compact` 후 복구되지 않는다.
8. 파일이 일정 크기를 넘으면 새 segment로 전환된다.

## 구현 단계

### 1단계: 전체 파일 스캔

```text
data.txt
key=value
key=value
```

문제:

- 조회할 때마다 전체 파일을 읽어야 한다.

### 2단계: append-only log

```text
SET user:1 Daniel
SET user:2 Alice
SET user:1 Daniel2
DEL user:2
```

관찰:

- 쓰기는 단순해진다.
- 같은 key의 과거 값이 계속 쌓인다.

### 3단계: in-memory hash index

```kotlin
Map<String, Long> keyOffsetIndex
```

관찰:

- 읽기는 빨라진다.
- 인덱스 복구 비용과 메모리 사용량이 생긴다.

### 4단계: compaction

최신 값과 tombstone만 남긴다.

관찰:

- 디스크 사용량이 줄어든다.
- compaction 중 장애가 나면 어떤 파일이 진짜인지 결정해야 한다.

### 5단계: segment file

```text
segment-0001.log
segment-0002.log
segment-0003.log
```

관찰:

- active segment와 immutable segment를 구분하게 된다.
- Bitcask/LSM Tree 감각으로 이어진다.

## 완료 조건

- 모든 테스트가 통과한다.
- `notes.md`에 아래 3개를 적는다.
  - 전체 스캔 방식의 병목
  - hash index가 해결한 것과 새로 만든 문제
  - compaction에서 tombstone이 필요한 이유
