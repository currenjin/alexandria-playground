# 데모 실행 방법

## 기본 실행
다음 명령어로 오늘 날짜 기준 리마인더를 출력합니다.

```bash
./gradlew runDemo
```

## 날짜 지정 실행
테스트용 날짜를 지정하려면 `-Ptoday` 옵션을 사용합니다.

```bash
./gradlew runDemo -Ptoday=2026-01-10
```

## 출력 예시

```text
D-3: Netflix subscription is due on 2026-01-15. Amount: 14000
```
