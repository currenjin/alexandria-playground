# 에이전트 오케스트레이션

## 사용 가능한 에이전트

| 에이전트 | 목적 | 사용 시기 |
|---------|------|----------|
| **planner** | 구현 계획 수립 | 복잡한 기능, 리팩토링 |
| **architect** | 시스템 설계 | 아키텍처 결정 |
| **tdd-guide** | 테스트 주도 개발 | 새 기능, 버그 수정 |
| **code-reviewer** | 코드 리뷰 | 코드 작성 후 |
| **security-reviewer** | 보안 분석 | 커밋 전 |
| **build-error-resolver** | 빌드 에러 수정 | 빌드 실패 시 |
| **e2e-runner** | E2E 테스트 | 주요 사용자 흐름 |
| **refactor-cleaner** | 불필요 코드 제거 | 코드 정리 |
| **doc-updater** | 문서 업데이트 | 문서 동기화 |

## 즉시 에이전트 사용

사용자 요청 없이 자동으로 사용해야 하는 경우:

| 상황 | 사용할 에이전트 |
|------|----------------|
| 복잡한 기능 요청 | **planner** |
| 코드 작성/수정 완료 | **code-reviewer** |
| 버그 수정 또는 새 기능 | **tdd-guide** |
| 아키텍처 결정 필요 | **architect** |
| 보안 관련 코드 변경 | **security-reviewer** |
| 빌드 실패 | **build-error-resolver** |

## 병렬 작업 실행

독립적인 작업은 항상 병렬로 실행한다.

```markdown
# Good - 병렬 실행
3개 에이전트 동시 실행:
1. Agent 1: auth 모듈 보안 분석
2. Agent 2: cache 시스템 성능 리뷰
3. Agent 3: utils 타입 체크

# Bad - 불필요한 순차 실행
먼저 Agent 1, 그 다음 Agent 2, 그 다음 Agent 3
```

## 다중 관점 분석

복잡한 문제는 여러 역할의 서브 에이전트로 분석:

- **사실 검토자** - 정확성 확인
- **시니어 엔지니어** - 설계 검토
- **보안 전문가** - 보안 취약점 확인
- **일관성 검토자** - 코드 스타일 일관성
- **중복 검사자** - 불필요한 코드 확인

## 워크플로우별 에이전트 조합

### 새 기능 개발

```
1. planner → 구현 계획 수립
2. tdd-guide → 테스트 우선 작성
3. (구현)
4. code-reviewer → 코드 리뷰
5. security-reviewer → 보안 검토 (필요시)
```

### 버그 수정

```
1. tdd-guide → 실패 테스트 작성
2. (수정)
3. code-reviewer → 코드 리뷰
```

### 리팩토링

```
1. planner → 리팩토링 계획
2. refactor-cleaner → 불필요 코드 확인
3. (리팩토링)
4. code-reviewer → 코드 리뷰
```

### 아키텍처 변경

```
1. architect → 설계 검토
2. planner → 구현 계획
3. (구현)
4. code-reviewer → 코드 리뷰
5. security-reviewer → 보안 검토
```

## 에이전트 사용 팁

1. **계획 먼저**: 복잡한 작업 전 항상 `planner` 사용
2. **테스트 우선**: 구현 전 `tdd-guide`로 테스트 먼저
3. **리뷰 습관화**: 코드 작성 후 즉시 `code-reviewer` 실행
4. **보안 확인**: 인증/인가 관련 코드는 `security-reviewer` 필수
5. **병렬 활용**: 독립적인 작업은 최대한 병렬로 실행
