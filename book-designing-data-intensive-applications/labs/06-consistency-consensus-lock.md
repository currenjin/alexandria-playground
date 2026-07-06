# Lab 06. Leader Election과 Lock Server

연결 챕터: Ch.9 Consistency and Consensus

## 목표

Raft 전체 구현이 아니라, leader election과 fencing token을 작은 코드로 이해한다.

## 구성

```text
node-a
node-b
node-c
```

## TDD 순서

1. leader가 heartbeat를 보낸다.
2. follower는 heartbeat가 끊기면 candidate가 된다.
3. 새 election이 시작되면 term이 증가한다.
4. 더 높은 term을 본 노드는 예전 leader를 거부한다.
5. lock acquire 시 fencing token이 증가한다.
6. 오래된 fencing token으로 write하면 거부된다.

## Lock Server API

```text
POST /locks/{name}/acquire
POST /locks/{name}/release
```

응답:

```json
{ "lockName": "order:1", "owner": "client-a", "fencingToken": 7 }
```

## 일부러 깨뜨릴 것

- split brain
- lock을 잡은 클라이언트가 멈춘 뒤 늦게 write하는 상황
- lease 만료와 clock skew 문제

## 완료 조건

- fencing token 없이 깨지는 테스트가 있다.
- fencing token으로 오래된 write를 막는 테스트가 있다.
- leader election과 linearizability의 관계를 짧게 정리한다.
