# Lab 07. 스키마 진화와 호환성

연결 챕터: Ch.4 Encoding and Evolution

## 목표

이벤트 스키마를 v1에서 v2로 바꿔도 구버전/신버전 reader가 안전하게 동작하는지 확인한다.

## 이벤트 예시

v1:

```json
{
  "userId": "1",
  "name": "Daniel"
}
```

v2:

```json
{
  "userId": "1",
  "name": "Daniel",
  "email": "a@b.com"
}
```

## TDD 순서

1. v2 reader는 v1 event를 읽을 수 있다.
2. v1 reader는 v2 event에서 모르는 필드를 무시한다.
3. 필수 필드를 제거하면 호환성이 깨진다.
4. default value가 있으면 누락 필드를 안전하게 처리한다.
5. producer/consumer 배포 순서를 바꿔도 실패하지 않는다.

## 구현 단계

1. Jackson JSON으로 시작한다.
2. optional/default 정책을 정한다.
3. 가능하면 Protobuf 또는 Avro 버전을 추가한다.
4. rolling deploy 순서를 문서화한다.

## 완료 조건

- backward/forward compatibility 테스트가 있다.
- 깨지는 스키마 변경 사례 1개가 있다.
- 무중단 배포 순서를 적는다.
