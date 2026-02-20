# Redis 분산락 패턴

Redis의 `SET key value NX PX ttl`와 Lua 스크립트를 이용해 안전한 분산락을 구현한 프로젝트입니다.

## 핵심 아이디어

- 락 획득: `SET lockKey token NX PX ttl`
- 락 해제: 토큰이 일치할 때만 삭제 (`compare-and-delete` Lua)
- 락 갱신: 토큰이 일치할 때만 TTL 연장 (`compare-and-expire` Lua)

토큰 기반 비교를 사용하므로, 만료 후 다른 노드가 획득한 락을 이전 소유자가 잘못 해제하는 문제를 방지할 수 있습니다.

## 구조

- `core/RedisDistributedLock`: 락 획득/해제/갱신 오케스트레이션
- `core/LockToken`: 락 소유자 식별 토큰(UUID)
- `redis/RedisLockCommands`: Redis 연산 인터페이스
- `redis/RedisLuaScripts`: 원자적 해제/갱신에 필요한 Lua 스크립트
- `test/InMemoryRedisLockCommands`: 테스트용 Redis 시뮬레이터

## 테스트 실행

```bash
./gradlew test
```

## Redis 실행 (로컬)

```bash
docker compose up -d
```

기본 포트 `6379`로 Redis 7 컨테이너를 띄웁니다.
