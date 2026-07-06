# Lab 05. 분산 시스템 장애 주입

연결 챕터: Ch.8 The Trouble with Distributed Systems

## 목표

네트워크와 노드는 믿을 수 없다는 것을 코드로 재현한다.

## 장애 종류

- 응답 지연
- 요청 유실
- 중복 요청
- 노드 다운
- 시계 차이

## TDD 순서

1. replica 응답이 늦으면 primary는 timeout 처리한다.
2. timeout 이후 재시도하면 중복 write가 생길 수 있다.
3. request id를 넣으면 중복 write를 막을 수 있다.
4. retry with backoff가 즉시 재시도보다 부하를 덜 만든다.
5. clock skew가 있으면 timestamp 기반 판단이 틀릴 수 있다.

## 구현 방법

- Spring Filter/Interceptor로 랜덤 delay를 넣는다.
- Docker Compose로 컨테이너를 중단한다.
- 필요하면 Toxiproxy로 네트워크 지연/단절을 만든다.

## 완료 조건

- timeout, retry, duplicate write를 각각 재현한다.
- 멱등키 적용 전/후 결과를 비교한다.
- `네트워크 분할이 생기면 어떤 보장을 포기하는가`를 적는다.
