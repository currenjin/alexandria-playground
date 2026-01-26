# 코딩 스타일 원칙

## 불변성 (Immutability) - 핵심 원칙

항상 새 객체를 생성하고, 절대 기존 객체를 변경하지 않는다.

### 불변 객체 사용

```kotlin
// Bad - 가변
class Order {
    var status: OrderStatus = OrderStatus.PENDING
    var updatedAt: LocalDateTime = LocalDateTime.now()
}

// Good - 불변
data class Order(
    val status: OrderStatus,
    val updatedAt: LocalDateTime
) {
    fun updateStatus(newStatus: OrderStatus): Order {
        return this.copy(
            status = newStatus,
            updatedAt = LocalDateTime.now()
        )
    }
}
```

### val 우선 사용

`var` 대신 `val`을 기본으로 사용한다.

```kotlin
// Bad
var total = 0
for (item in items) {
    total += item.price
}

// Good
val total = items.sumOf { it.price }
```

### 컬렉션 불변성

가변 컬렉션보다 불변 컬렉션을 선호한다.

```kotlin
// Bad
val orderList = mutableListOf<Order>()
orderList.add(order)

// Good
val orderList = listOf(order)
// 또는 추가가 필요한 경우
val newOrderList = orderList + order
```

## 파일 구조

### 파일 크기 기준

- **권장**: 200-400줄
- **최대**: 800줄
- 큰 파일은 유틸리티 추출하여 분리

### 파일당 하나의 Public 클래스

- 파일 하나에 Public 클래스는 하나만 선언
- 관련된 Private/Internal 클래스는 같은 파일에 포함 가능

### 파일 네이밍

| 유형 | 네이밍 | 예시 |
|------|--------|------|
| Entity | `<Name>.kt` | `Order.kt` |
| Service | `<Name><Role>.kt` | `OrderSaver.kt`, `OrderReader.kt` |
| Repository | `<Name>Repository.kt` | `OrderRepository.kt` |
| DTO | `<Name>Dto.kt` | `OrderDto.kt` |
| Controller | `<Name>Controller.kt` | `OrderController.kt` |
| Facade | `<Name>Facade.kt` | `OrderFacade.kt` |

## 에러 처리

항상 에러를 명시적으로 처리한다.

```kotlin
// Bad - 에러 무시
fun processOrder(orderId: Long) {
    val order = orderRepository.findById(orderId)
    // order가 null일 수 있음
    order.process()
}

// Good - 명시적 에러 처리
fun processOrder(orderId: Long) {
    val order = orderRepository.findById(orderId)
        ?: throw OrderNotFoundException("주문을 찾을 수 없습니다: $orderId")
    order.process()
}

// Good - Result 타입 사용
fun processOrder(orderId: Long): Result<Order> {
    return runCatching {
        val order = orderRepository.getById(orderId)
        order.process()
    }
}
```

## 함수 설계

### 단일 책임

하나의 함수는 하나의 작업만 수행한다.

```kotlin
// Bad - 여러 책임
fun processOrder(order: Order) {
    validateOrder(order)
    saveOrder(order)
    sendNotification(order)
    updateInventory(order)
}

// Good - 단일 책임
fun validateOrder(order: Order): ValidationResult { ... }
fun saveOrder(order: Order): Order { ... }
fun sendNotification(order: Order) { ... }
fun updateInventory(order: Order) { ... }
```

### 함수 크기

- **권장**: 50줄 미만
- 긴 함수는 작은 함수들로 분리

### 부수 효과 최소화

순수 함수를 선호하고, 부수 효과가 있는 함수는 명확히 표시한다.

```kotlin
// 순수 함수 - 같은 입력에 항상 같은 출력
fun calculateTotal(items: List<Item>): Int {
    return items.sumOf { it.price }
}

// 부수 효과 있음 - 네이밍으로 표시
fun saveAndNotify(order: Order) { ... }
```

## 코드 품질 체크리스트

작업 완료 전 확인:

- [ ] 코드가 읽기 쉽고 네이밍이 명확한가
- [ ] 함수가 작은가 (<50줄)
- [ ] 파일이 집중되어 있는가 (<800줄)
- [ ] 깊은 중첩이 없는가 (≤4단계)
- [ ] 에러 처리가 적절한가
- [ ] 디버그용 로그가 제거되었는가
- [ ] 하드코딩된 값이 없는가
- [ ] 불변 패턴을 사용했는가 (mutation 없음)
- [ ] 테스트가 작성되었는가
