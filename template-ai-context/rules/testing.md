# 테스트 가이드

## 기본 원칙

- **모든 @Service 클래스에 테스트 필수**
- **최소 80% 라인 커버리지** 유지
- 핵심 비즈니스 로직은 100% 커버리지 목표

> TDD 워크플로우가 필요하면 `tdd-workflow` 스킬을 활성화하세요.

## 테스트 필수 대상

```kotlin
@Service
class OrderSaver(private val orderRepository: OrderRepository) {
    fun save(dto: OrderServiceDto): Order { ... }
}

// 반드시 테스트 작성
class OrderSaverTest {
    @Test
    fun `주문을 저장한다`() { ... }
}
```

## 테스트 분류

| 유형 | 대상 | 범위 |
|------|------|------|
| Unit Test | Service, Domain | 단일 클래스 |
| Integration Test | Repository, External API | 여러 컴포넌트 |
| E2E Test | Controller | 전체 흐름 |

## 테스트 구조

### Given-When-Then 패턴

```kotlin
@Test
fun `재고가 부족하면 주문 생성에 실패한다`() {
    // Given
    val product = Product(id = 1L, stock = 5)
    val orderRequest = OrderRequest(productId = 1L, quantity = 10)

    // When & Then
    assertThrows<InsufficientStockException> {
        orderService.createOrder(orderRequest)
    }
}
```

### 테스트 네이밍

- 백틱(`) 사용하여 한글로 테스트 의도 명시

```kotlin
// Good
@Test
fun `주문 상태가 PENDING이면 취소할 수 있다`() { ... }

// Bad
@Test
fun testCancelOrder() { ... }
```

## 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 단일 테스트 클래스
./gradlew :모듈이름:test --tests "전체클래스경로"

# 커버리지 리포트
./gradlew jacocoTestReport
```

## Mock 사용

### MockK 패턴

```kotlin
class OrderSaverTest {
    @MockK
    private lateinit var orderRepository: OrderRepository

    private lateinit var sut: OrderSaver

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        sut = OrderSaver(orderRepository)
    }

    @Test
    fun `주문을 저장한다`() {
        // Given
        val order = Order(productId = 1L, quantity = 5)
        every { orderRepository.save(any()) } returns order

        // When
        val result = sut.save(order.toServiceDto())

        // Then
        assertThat(result).isEqualTo(order)
        verify { orderRepository.save(any()) }
    }
}
```

## 테스트해야 할 Edge Cases

1. **Null/Empty** - null 값, 빈 컬렉션
2. **경계값** - 최소/최대값
3. **잘못된 입력** - 유효하지 않은 타입
4. **에러 상황** - 네트워크 오류, DB 오류
