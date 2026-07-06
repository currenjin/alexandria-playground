# Lab 03. Primary-Replica 복제

연결 챕터: Ch.5 Replication

## 목표

primary에 쓴 데이터가 replica로 비동기 복제되는 구조를 직접 만든다.

## 구성

```text
primary
replica-1
replica-2
```

처음에는 Spring Boot 앱 3개 또는 같은 프로세스 안의 3개 노드 객체로 시작해도 된다.

## TDD 순서

1. primary에 write하면 primary에서는 바로 read된다.
2. write 직후 replica에서 read하면 값이 없을 수 있다.
3. 시간이 지나면 replica가 primary를 따라잡는다.
4. replica lag를 측정할 수 있다.
5. read-your-writes 옵션을 켜면 방금 쓴 값은 primary에서 읽는다.

## 구현 단계

1. primary의 append log를 만든다.
2. replica가 log offset을 따라가게 한다.
3. 인위적으로 replica delay를 넣는다.
4. lag metric을 노출한다.
5. read routing 정책을 추가한다.

## 관찰할 문제

- replication lag
- eventual consistency
- read-your-writes 깨짐
- primary 장애 시 쓰기 불가

## 완료 조건

- write 직후 replica stale read를 테스트로 재현한다.
- lag가 줄어드는 과정을 로그나 metric으로 남긴다.
- 동기 복제와 비동기 복제의 trade-off를 정리한다.
