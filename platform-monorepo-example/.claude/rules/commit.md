# 커밋 컨벤션

- **Conventional Commits**: `type(scope): 제목`
  - `type`: `feat`(기능) · `fix`(버그) · `refactor`(구조 변경·동작 불변) · `test` · `docs` · `chore` · `build` · `perf`
  - `scope`: 모듈명 — `oms`·`tms`·`bms`·`ems`·`core`·`contract`·`orchestrator` 등 (생략 가능)
- **구조 변경과 행동 변경을 한 커밋에 섞지 말 것** (Tidy First, → `tidy-first.md`): rename·move·extract 같은 구조 변경은 `refactor`로, 기능·버그는 `feat`/`fix`로 **분리 커밋**한다. 리뷰·롤백·원인 추적이 쉬워지고 "리팩터에 기능이 숨어드는" 사고를 막는다.
- 제목은 **명령형·간결하게**. "왜"는 본문에 남긴다.

## 예

- `feat(oms): 오더 취소 시 배차 보상 커맨드 발행`
- `refactor(core): OutboxRelay 배치 조회 쿼리 추출`
- `fix(core): payload 역직렬화 실패를 NonRetryable로 처리해 즉시 DLT`
- `test(orchestrator): 배송완주 사가 3단 체인 검증 추가`
