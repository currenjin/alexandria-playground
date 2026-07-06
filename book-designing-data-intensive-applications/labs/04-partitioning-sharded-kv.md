# Lab 04. Shard 3개짜리 KV Store

연결 챕터: Ch.6 Partitioning

## 목표

key를 기준으로 여러 shard에 데이터를 나누고, shard 수 변경과 hot key 문제를 관찰한다.

## 구성

```text
router
shard-0
shard-1
shard-2
```

## TDD 순서

1. 같은 key는 항상 같은 shard로 간다.
2. 서로 다른 key는 여러 shard에 분산된다.
3. shard 수를 3에서 4로 바꾸면 많은 key 위치가 바뀐다.
4. consistent hashing을 적용하면 일부 key만 이동한다.
5. 특정 key에 요청 90%를 몰면 hot partition이 드러난다.

## 구현 단계

1. `hash(key) % shardCount`로 시작한다.
2. shard별 request count를 측정한다.
3. shard 추가 시 이동한 key 비율을 계산한다.
4. consistent hashing ring을 만든다.
5. virtual node를 추가해 분포를 개선한다.

## 완료 조건

- naive hash와 consistent hashing의 key 이동 비율을 비교한다.
- hot key가 전체 처리량을 제한하는 상황을 테스트나 로그로 보인다.
- range partition과 hash partition 중 어떤 상황에 무엇이 나은지 적는다.
