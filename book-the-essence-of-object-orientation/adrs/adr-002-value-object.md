# ADR002. 값 객체(Value Object) 도입

## Context
도메인에서 Money, Address, DateRange 등 개념적으로 값인 것들이 primitive 타입이나 일반 객체로 표현되어 있다:
- 금액이 `long` 또는 `BigDecimal`로 직접 사용된다
- 동등성 비교가 명확하지 않다
- 관련 로직이 여러 곳에 분산되어 있다

## Decision
값 객체(Value Object)를 도입하여 도메인 개념을 명확히 표현한다:

1. **불변성**: 생성 후 상태가 변경되지 않는다
   ```java
   public final class Money {
       private final BigDecimal amount;
       private final Currency currency;

       public Money add(Money other) {
           return new Money(this.amount.add(other.amount), this.currency);
       }
   }
   ```

2. **동등성**: `equals()`와 `hashCode()`를 값으로 구현한다

3. **자기 완결성**: 관련 로직을 값 객체 내부에 포함한다

4. **식별 기준**:
   - 식별자가 필요 없다 → Value Object
   - 생명주기가 있고 식별자가 필요하다 → Entity

## Consequences

### Positive
- 도메인 개념이 명확해진다
- 불변성으로 인한 안전성이 보장된다
- 관련 로직이 응집된다
- 테스트가 용이하다

### Negative
- 상태 변경 시 새 객체 생성이 필요하다
- 초기에는 오버엔지니어링으로 보일 수 있다

### Risks
- 모든 것을 값 객체로 만들려는 과도한 적용을 주의해야 한다
