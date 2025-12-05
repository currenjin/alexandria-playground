# Mafia Server Playground

마피아류 게임의 **핵심 도메인 로직**을 Kotlin으로 모델링하고,  
REST API와 Netty TCP 서버 두 가지 방식으로 노출해 보는 토이 프로젝트입니다.

---

## Stack

- Language: Kotlin
- Framework
  - Spring Boot (REST API)
  - Netty (TCP 텍스트 서버)
- Storage: In-memory (`ConcurrentHashMap` 기반 `InMemoryGameRepository`)
- Test: JUnit (도메인/서비스 단위 테스트)

---

## Domain

핵심 Aggregate는 `Game`입니다.

도메인

- 방 생성 (`maxPlayers`)
- 플레이어 입장 (`join`)
- 게임 시작 (`start`) – 마피아/시민 역할 배정
- 낮/밤 턴 전환 (`turn`, `turnNumber`)
- 낮 투표 및 최다 득표자 사망 처리 (`vote`, `nextTurn`)
- 동점 시 아무도 죽지 않는 규칙

이 규칙들은 JUnit 기반 테스트(`GameTest`)로 먼저 정의한 뒤 구현했습니다.

---

## REST API (간단)

Spring Boot로 노출하는 최소한의 유즈케이스

- `POST /api/games` - 게임 생성
- `POST /api/games/{gameId}/join` - 플레이어 입장
- `POST /api/games/{gameId}/start` - 게임 시작
- `POST /api/games/{gameId}/vote` - 투표
- `POST /api/games/{gameId}/next-turn` - 턴 전환

로컬 실행

```bash
./gradlew bootRun
```

## Netty TCP Server

같은 `Game` 도메인을 Netty 기반 TCP 서버로도 노출해서,  
여러 터미널에서 실제 유저처럼 접속해 마피아 게임을 진행해볼 수 있습니다.

### 시연 영상
- [Youtube](https://youtu.be/6Ep73YpuBlI)

### 실행 방법

1. 서버 실행 (예: `MafiaNettyServer.main()` 실행)
2. 터미널 여러 개에서 접속

```bash
nc localhost 9000
```

접속하면 사용 가능한 명령 목록과 간단한 안내 메시지가 출력됩니다.

### 명령어

- `CREATE <maxPlayers>`  
  새 게임을 생성합니다. 응답으로 게임 ID가 반환됩니다.

- `JOIN <gameId> <playerId> <nickname>`  
  해당 게임에 플레이어를 입장시킵니다.

- `START <gameId>`  
  게임을 시작하고, 플레이어에게 마피아/시민 역할을 배정합니다.

- `VOTE <gameId> <voterId> <targetId>`  
  낮 턴에 투표를 진행합니다. 살아 있는 플레이어만 투표/피투표 가능합니다.

- `NEXT <gameId>`  
  턴을 전환합니다.  
  - DAY -> NIGHT: 투표 결과를 집계해 최다 득표자 1명이면 사망 처리, 동점이면 아무도 죽지 않습니다.  
  - NIGHT -> DAY: 다음 낮 턴으로 넘어갑니다.

- `STATE <gameId>`  
  현재 게임 상태를 조회합니다.  
  (턴, 턴 번호, 각 플레이어의 ID/닉네임/역할/생존 여부 등)

터미널 여러 개에서 위 명령을 사용하면  
여러 유저가 동시에 접속해 방 생성 -> 입장 -> 시작 -> 투표 -> 턴 전환 흐름을 직접 테스트해볼 수 있습니다.

