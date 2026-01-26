# Git 워크플로우

## 커밋 메시지 포맷

```
<type>: <description>

<optional body>
```

### 타입

| 타입 | 설명 |
|------|------|
| `feat` | 새로운 기능 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 (기능 변경 없음) |
| `docs` | 문서 수정 |
| `test` | 테스트 추가/수정 |
| `chore` | 빌드, 설정 등 기타 작업 |
| `perf` | 성능 개선 |
| `ci` | CI/CD 설정 |

### 예시

```bash
feat: 주문 취소 기능 추가

- 주문 상태가 PENDING일 때만 취소 가능
- 취소 시 재고 복구 로직 포함
```

```bash
fix: 주문 생성 시 재고 감소 버그 수정
```

```bash
refactor: OrderService를 Saver/Reader로 분리
```

## Pull Request 워크플로우

PR 생성 시:

1. **전체 커밋 히스토리 분석** (최신 커밋만이 아닌 전체)
2. **`git diff [base-branch]...HEAD`** 사용하여 모든 변경사항 확인
3. **종합적인 PR 요약** 작성
4. **테스트 플랜** 포함
5. 새 브랜치면 **`-u` 플래그**로 push

### PR 제목 형식

```
[타입] 간단한 설명
```

예시:
- `[feat] 주문 취소 기능 추가`
- `[fix] 재고 감소 버그 수정`
- `[refactor] Service 레이어 분리`

### PR 본문 템플릿

```markdown
## 요약
- 변경 사항 1
- 변경 사항 2

## 테스트 계획
- [ ] 단위 테스트 통과
- [ ] 통합 테스트 통과
- [ ] 수동 테스트 완료

## 관련 이슈
- #123
```

## 기능 구현 워크플로우

### 1. 계획 수립 (Plan First)

- **planner 에이전트** 사용하여 구현 계획 수립
- 의존성 및 위험 요소 식별
- 단계별 분해

### 2. TDD 접근

- **tdd-guide 에이전트** 사용
- 테스트 먼저 작성 (RED)
- 테스트 통과하는 구현 (GREEN)
- 리팩토링 (REFACTOR)
- 커버리지 80%+ 확인

### 3. 코드 리뷰

- 코드 작성 후 즉시 **code-reviewer 에이전트** 사용
- CRITICAL, HIGH 이슈 해결
- MEDIUM 이슈 가능하면 해결

### 4. 커밋 & 푸시

- 상세한 커밋 메시지 작성
- Conventional Commits 형식 준수
- `./gradlew ktlintFormat` 실행 후 커밋

## 브랜치 전략

### 브랜치 네이밍

```
<type>/<ticket-number>-<short-description>
```

예시:
- `feat/RP-1234-order-cancel`
- `fix/RP-5678-stock-bug`
- `refactor/RP-9012-service-split`

### 브랜치 종류

| 브랜치 | 용도 |
|--------|------|
| `main` | 프로덕션 배포 |
| `develop` | 개발 통합 |
| `feat/*` | 기능 개발 |
| `fix/*` | 버그 수정 |
| `hotfix/*` | 긴급 수정 |

## 체크리스트

커밋 전 확인:

- [ ] `./gradlew ktlintFormat` 실행
- [ ] `./gradlew test` 통과
- [ ] 커밋 메시지 형식 준수
- [ ] 관련 없는 파일 포함되지 않음
