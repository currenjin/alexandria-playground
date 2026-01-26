# 빌드/테스트 명령어

## 빌드

```bash
# 전체 빌드
./gradlew clean build

# 빌드 (테스트 제외)
./gradlew clean build -x test
```

## 테스트

```bash
# 전체 테스트
./gradlew test

# 단일 테스트
./gradlew :모듈이름:test --tests "전체클래스경로"

# 예시
./gradlew :order:application:test --tests "com.wemeet.roouty.order.application.service.OrderReaderTest"
```

## 코드 스타일

```bash
# 린트 검사
./gradlew ktlintCheck

# 자동 포맷팅
./gradlew ktlintFormat
```

## 참고사항

- pre-commit 훅으로 코드 스타일 자동 검사가 실행됨
- 커밋 전 `ktlintFormat` 실행 권장
