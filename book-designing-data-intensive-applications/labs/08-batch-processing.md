# Lab 08. 로그 파일 배치 처리

연결 챕터: Ch.10 Batch Processing

## 목표

로그 파일을 읽어 집계하고, chunk/parallel/map-reduce 방식으로 확장한다.

## 입력

```text
access.log
```

예시:

```text
2026-07-06T10:00:00Z user-1 GET /orders 200 31ms
2026-07-06T10:00:01Z user-2 GET /products 500 120ms
```

## 산출

- 유저별 요청 수
- URL별 요청 수
- 에러율
- p95 latency

## TDD 순서

1. 로그 1줄을 파싱한다.
2. 로그 1000줄을 처리하면 URL별 count가 정확하다.
3. chunk 순서가 바뀌어도 결과는 같다.
4. 같은 입력을 재처리해도 결과는 같다.
5. 잘못된 로그 라인은 dead-letter 파일로 분리한다.

## 구현 단계

1. 단일 프로세스 처리
2. chunk 나누기
3. parallel 처리
4. map/reduce 흉내
5. 재처리/실패 라인 처리

## 완료 조건

- 입력 순서가 달라도 결과가 같은지 검증한다.
- 재처리해도 중복 집계되지 않는 방식을 설명한다.
- batch job의 idempotency를 적는다.
