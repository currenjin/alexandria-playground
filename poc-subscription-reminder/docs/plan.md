# Subscription Billing Reminder - plan.md

## 1. 목표
사용자가 등록한 구독(예: Netflix, YouTube Premium, 도메인 등)의 **다음 결제일(nextBillingDate)** 을 계산하고,
오늘 날짜 기준으로 **D-7 / D-3 / D-1 / D-day**에 해당하는 리마인더 목록을 생성한다.

---

## 2. MVP 범위
### 포함
- Subscription 모델 정의 및 목록 관리(초기에는 메모리/하드코딩 가능)
- 결제 주기별 nextBillingDate 계산
    - Monthly / Yearly / Every N Days
- 오늘 기준 리마인더 후보 생성
    - offsets 기본값: [7, 3, 1, 0]
- 알림 메시지 템플릿 생성
- JUnit 테스트(날짜 경계 포함)
- 데모 실행 방식 1개(콘솔 출력 또는 간단 API)

### 제외
- 실제 알림 발송(Email/Slack/Push 등)
- 사용자/인증, DB 영속화
- UI/프론트엔드
- 복잡한 타임존 처리(시간 단위는 다루지 않음)

---

## 3. 핵심 정책(결정 사항)
### 3.1 날짜 타입
- `LocalDate`만 사용한다. (시간/타임존 배제)
- `today`는 테스트에서 주입 가능한 값으로 취급한다.

### 3.2 nextBillingDate 기준
- `nextBillingDate`는 **today와 같거나 이후(>= today)** 인 결제일 중 가장 빠른 날짜

### 3.3 월간(Monthly) 보정 규칙
- “매월 31일”처럼 해당 월에 dayOfMonth가 없으면 **그 달의 마지막 날**로 보정한다.
    - 예: 2026-01-31의 다음 결제일(2월) = 2026-02-28 (윤년이면 2/29)

### 3.4 연간(Yearly) 보정 규칙
- “매년 2/29”처럼 해당 연도에 날짜가 없으면 **그 달의 마지막 날**로 보정한다.
    - 예: 2024-02-29 → 2026년은 2026-02-28
    - 윤년인 해에는 2/29로 복귀(예: 2028-02-29)

### 3.5 Every N Days 규칙
- firstBillingDate를 기준으로 n일 간격으로 반복한다고 가정한다.
- nextBillingDate는 다음 조건을 만족하는 최소의 k에 대해 `first + k*n` 으로 계산한다:
    - `first + k*n >= today`

### 3.6 리마인더 오프셋 규칙
- 기본 오프셋: `[7, 3, 1, 0]`
- 오늘 생성하는 리마인더는 `dDay = DAYS.between(today, dueDate)` 가 오프셋에 포함될 때만 생성한다.

---

## 4. 도메인 모델(초안)
### 4.1 Subscription
- id: Long
- name: String
- amount: Int? (optional)
- cycle: BillingCycle
- firstBillingDate: LocalDate
- reminderOffsets: List<Int> = [7, 3, 1, 0]

### 4.2 BillingCycle
- Monthly(dayOfMonth: Int)  // 또는 firstBillingDate에서 자동 추출
- Yearly(month: Int, dayOfMonth: Int)
- EveryNDays(days: Int)

### 4.3 ReminderCandidate
- subscriptionId: Long
- dueDate: LocalDate
- dDay: Int
- message: String

---

## 5. 외부 인터페이스(데모)
### 옵션 A: 콘솔
- main() 실행 시, 샘플 구독 목록을 기반으로 `today` 기준 리마인더를 출력한다.

### 옵션 B: REST API
- GET /reminders/today?today=2026-01-10
- (선택) GET /subscriptions

---

## 6. Definition of Done (완료 기준)
- [x] 결제 주기 3종에 대해 nextBillingDate 계산이 정확히 동작한다.
- [x] 월말/윤년 보정 규칙이 테스트로 검증된다.
- [x] dDay 오프셋 규칙에 맞는 리마인더만 생성된다.
- [x] 메시지 템플릿이 amount 유무에 따라 자연스럽게 출력된다.
- [x] 핵심 로직은 테스트로 보호된다(최소 15개 케이스).
- [x] 데모 실행 방법이 문서화되어 있다.

---

## 7. 구현 순서(권장)
1) 프로젝트/테스트 셋업
2) 도메인 모델 정의
3) nextBillingDate 계산 로직 구현
4) 리마인더 추출 로직 구현
5) 메시지 템플릿 구현
6) 데모(main 또는 API) 작성
7) 리팩토링 + 문서 정리

---

## 8. 테스트 케이스(초안 15개)
### Monthly
- [x] first=2026-01-15, today=2026-01-10 -> next=2026-01-15
- [x] first=2026-01-15, today=2026-01-15 -> next=2026-01-15
- [x] first=2026-01-15, today=2026-01-16 -> next=2026-02-15
- [x] first=2026-01-31, today=2026-02-01 -> next=2026-02-28 (월말 보정)
- [x] first=2026-01-31, today=2026-03-01 -> next=2026-03-31

### Yearly
- [x] first=2020-06-10, today=2026-01-10 -> next=2026-06-10
- [x] first=2020-06-10, today=2026-06-10 -> next=2026-06-10
- [x] first=2020-06-10, today=2026-06-11 -> next=2027-06-10
- [x] first=2024-02-29, today=2026-01-10 -> next=2026-02-28 (윤년 보정)
- [x] first=2024-02-29, today=2028-02-28 -> next=2028-02-29 (윤년 복귀)

### Every N Days
- [x] first=2026-01-01, n=30, today=2026-01-10 -> next=2026-01-31
- [x] first=2026-01-01, n=30, today=2026-01-31 -> next=2026-01-31
- [x] first=2026-01-01, n=30, today=2026-02-01 -> next=2026-03-02

### Reminder extraction
- [x] due=today+3, offsets=[7,3,1,0] -> D-3 리마인더 생성
- [x] due=today+2, offsets=[7,3,1,0] -> 리마인더 생성 안 됨
