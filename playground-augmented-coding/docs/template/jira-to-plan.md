---
model: sonnet
---

# JIRA to TDD Plan Generator

JIRA 이슈 티켓을 읽어서 TDD 구현 계획(plan)을 생성합니다.

## 사용법

```
/plan-from-jira RP-1234
```

## 동작 방식

### 1. JIRA 이슈 읽기

MCP를 통해 JIRA 이슈 정보를 가져옵니다:
- 제목
- 설명
- Acceptance Criteria
- 관련 정보

**MCP가 설정되지 않은 경우**: 사용자에게 JIRA 티켓 내용을 붙여넣도록 요청합니다.

### 2. 코드베이스 분석

관련 코드를 탐색하여 파악합니다:
- 수정이 필요한 파일
- 관련 도메인/서비스
- 기존 테스트 패턴

### 3. TDD Plan 생성

`.claude/skills/augmented-coding/` 디렉토리에 plan 파일을 생성합니다.

**파일명 규칙**: `{티켓번호}-plan.md`

예시: `RP-1234-plan.md`

## Plan 파일 형식

```markdown
# {티켓번호}: {제목}

## 개요
{JIRA 티켓 요약}

## 요구사항
- {AC 또는 요구사항 목록}

---

## TDD 구현 계획

### Phase 1: {단계명}

- [ ] **Task 1.1**: {기능 설명}
  - Test: `{테스트 파일 경로}`
    - {테스트 케이스 설명}
  - Impl: `{구현 파일 경로}`
    - {구현 설명}

- [ ] **Task 1.2**: {기능 설명}
  - Test: `{테스트 파일 경로}`
    - {테스트 케이스 설명}
  - Impl: `{구현 파일 경로}`
    - {구현 설명}

### Phase 2: {단계명}

...

---

## 파일 변경 목록

| 파일 | 변경 유형 |
|------|----------|
| `{경로}` | 수정/신규 |
```

## Task 작성 원칙

### 태스크 크기 (Tidy First 원칙)

- **하나의 Task = 하나의 Red → Green 사이클**
- 10-30분 내에 완료 가능한 크기로 분해
- 하나의 테스트 케이스 또는 밀접하게 관련된 2-3개의 테스트
- 복잡한 기능은 여러 작은 태스크로 분해
- 리팩토링(Tidy)은 별도 태스크로 분리

**Bad - 너무 큰 태스크:**
```markdown
- [ ] **Task 1**: 주문 생성 기능 구현
```

**Good - 작은 태스크로 분해:**
```markdown
- [ ] **Task 1.1**: 주문 엔티티 생성
- [ ] **Task 1.2**: 주문 저장 (OrderSaver)
- [ ] **Task 1.3**: 재고 검증 로직
- [ ] **Task 1.4**: 주문 생성 API 엔드포인트
- [ ] **Task 1.5**: 주문 생성 API Docs 추가
```

### API 작업 시 필수 태스크

API를 추가하는 작업의 경우, 다음 순서로 태스크를 구성합니다:

1. Domain/Service 레이어 구현 (단위 테스트)
2. Controller 구현 (API 테스트)
3. **API Docs 추가** (API 테스트가 완료된 후)

```markdown
### Phase N: API 레이어

- [ ] **Task N.1**: Controller 엔드포인트 구현
  - Test: `{Controller 테스트 경로}`
  - Impl: `{Controller 경로}`

- [ ] **Task N.2**: API Docs 추가
  - Test: RestDocs 스니펫 생성 확인
  - Impl: 문서화 어노테이션 추가
```

### 일반 원칙

- Test와 Impl은 분리하지 않고 하나의 태스크로 묶습니다
- 각 Task는 독립적으로 완료 가능해야 합니다
- Task 완료 후 리팩토링은 `refactor` 명령으로 별도 진행

## 주의사항

- MCP 설정이 필요합니다. `.claude/docs/mcp-setup.md` 참고
- 생성된 plan은 `/augmented-coding`에서 선택하여 사용합니다
- plan 파일은 수동으로 수정 가능합니다
