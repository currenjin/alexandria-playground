# Tidy First (변경 위생)

Kent Beck의 Tidy First — 작은 단위로 자주, **구조 변경과 행동 변경을 분리**해서 바꾼다.

## 변경 유형 분리 (절대 규칙)

| 유형 | 뜻 | 예 |
| --- | --- | --- |
| **구조적 변경** | 동작은 그대로, 코드 모양만 개선 | Extract Method, Rename, Move, 패키지 재편, Dead Code 제거 |
| **행동적 변경** | 기능 추가·수정 | 새 기능, 버그 수정, 로직 변경 |

- **둘을 한 커밋에 섞지 않는다.** 구조 변경 먼저(그리고 별도 커밋), 그 위에 행동 변경.
- 리뷰·롤백·원인 추적이 쉬워지고, "리팩토링에 기능이 숨어드는" 사고를 막는다.

## 자주 쓰는 tidying

| 패턴 | 뜻 |
| --- | --- |
| Guard Clause | 중첩 조건을 early return으로 평탄화 |
| Extract Method | 긴 메소드에서 의미 단위를 추출 |
| Rename | 의도가 드러나는 이름으로 |
| Remove Dead Code | 안 쓰는 코드 제거 |
| Normalize Symmetry | 비슷한 코드를 일관된 모양으로 |
| Move Declaration Closer | 선언을 사용처 가까이로 |

> 예: "M1(활성화=행동)"과 "common.event 재편(구조)"은 원칙상 **별도 커밋**이어야 한다.
