# TODO

## 다중 통화를 지원하는 Money 객체

### 예시 보고서

|종목|주|가격|합계|
|------|---|---|---|
|IBM|1000|25|25000|
|GE|400|100|40000|
| | |합계|65000|

다중통화 지원 보고서를 위한 통화 단위 추가)

|종목|주|가격|합계|
|------|---|---|---|
|IBM|1000|25USD|25000USD|
|Novartis|400|150CHF|60000CHF|
| | |합계|65000USD|

환율 명시)

|기준|변환|환율|
|------|---|---|
|CHF|USD|1.5|

### Tasks
- [x] $5 + 10CHF = $10(환율이 2:1일 경우)
- [x] $5 + $5 = $10
- [ ] $5 + $5 에서 Money 반환하기
- [x] Bank.reduce(Money)
- [x] Money 에 대한 통화 변환을 수행하는 Reduce
- [x] Reduce(Bank, String)
- [x] Sum.plus
- [x] Expression.times
- [x] $5 x 2 = $10
- [x] amount를 private으로 만들기
- [x] Dollar 부작용(side effect)?
- [ ] Money 반올림?
- [x] equals()
- [ ] hashCode()
- [ ] Equal null
- [ ] Equal object
- [x] 5CHF * 2 = 10CHF
- [x] Dollar/Franc 중복
- [x] 공용 equals
- [x] 공용 times
- [x] Franc과 Dollar 비교하기
- [x] 통화?
- [x] testFrancMultiplication을 지워야 할까?

### Acceptance criteria
- [ ] 통화가 다른 두 금액을 더해서 주어진 환율에 맞게 변한 금액을 결과로 얻을 수 있어야 한다.
- [ ] 어떤 금액(주가)을 어떤 수(주식의 수)에 곱한 금액을 결과로 얻을 수 있어야 한다.