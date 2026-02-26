# playground-playwright-usage

Playwright 사용법을 빠르게 익히기 위한 미니 프로젝트.

## 목표
- Playwright 설치/실행 흐름 이해
- 기본 E2E 테스트 작성
- `locator`, `expect`, `trace`를 직접 써보기

## 요구사항
- Node.js 20+

## 설치
```bash
npm install
npx playwright install
```

## 실행
```bash
npm test
```

UI 모드:
```bash
npm run test:ui
```

디버그 모드:
```bash
npm run test:debug
```

리포트 보기:
```bash
npm run report
```

## 테스트 구성
- `tests/home.spec.ts`
  - Playwright 공식 사이트 타이틀 검증
  - Get started 링크 이동 검증
- `tests/todo.spec.ts`
  - TodoMVC 예제로 입력/완료 체크

## 학습 체크리스트
1. `locator` vs `getByRole` 차이 이해
2. `expect(...).toHaveText()`로 안정적인 검증 작성
3. 실패 테스트를 일부러 만든 뒤 `trace.zip`으로 원인 확인
4. `test.only` / `test.describe` 활용

## 다음 확장
- GitHub Actions에 Playwright 워크플로우 추가
- 페이지 객체 모델(POM) 구조 분리
- 실제 서비스 로그인 시나리오 추가
