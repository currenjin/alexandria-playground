# Tidy First 원칙

## 핵심 개념

Kent Beck의 Tidy First 접근법: 작은 단위로 자주, 구조와 행동을 분리하여 변경한다.

## 변경 유형 분리 (절대 규칙)

| 유형 | 설명 | 예시 |
|------|------|------|
| **구조적 변경 (structural)** | 동작 변경 없이 코드 구조 개선 | Extract Method, Rename, Move |
| **행동적 변경 (behavioral)** | 기능 추가 또는 수정 | 새 기능, 버그 수정 |

**절대 섞지 마세요!** 구조적 변경과 행동적 변경은 반드시 별도 커밋으로 분리합니다.

## Tidying 패턴

| 패턴 | 설명 |
|------|------|
| **Guard Clause** | 중첩된 조건문을 early return으로 평탄화 |
| **Extract Method** | 긴 메서드에서 의미 있는 단위를 추출 |
| **Rename Variable/Method** | 의도를 명확히 드러내는 이름으로 변경 |
| **Remove Dead Code** | 사용되지 않는 코드 제거 |
| **Normalize Symmetry** | 비슷한 코드를 일관된 구조로 정리 |
| **Move Declaration Closer** | 변수 선언을 사용 위치 가까이로 이동 |
| **Extract Constant** | 매직 넘버를 의미 있는 상수로 추출 |
| **Inline** | 불필요한 간접 참조 제거 |
| **Chunk Statements** | 관련 있는 문장들을 빈 줄로 그룹화 |
| **Explaining Comments** | 복잡한 코드의 의도나 맥락을 설명하는 주석 추가 |
| **Delete Redundant Comments** | 코드가 이미 명확히 표현하는 내용을 반복하는 주석 삭제 |

## 태스크 크기 원칙

- **하나의 Task = 하나의 Red → Green 사이클**
- 각 태스크는 10-30분 내에 완료 가능한 크기
- 하나의 테스트 케이스 또는 밀접하게 관련된 2-3개의 테스트
- 복잡한 기능은 여러 작은 태스크로 분해
- 리팩토링(Tidy)은 별도 태스크/커밋으로 분리

## 우선순위

1. 중복 제거 (DRY)
2. 명확성 향상 (의도를 드러내는 코드)
3. 복잡도 감소 (단순화)
