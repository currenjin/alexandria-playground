# 아키텍처 가이드

## 레이어 구조

```
Controller → Facade → Service → Repository → Domain
```

## 레이어별 책임

### Controller 레이어

- HTTP 요청/응답 처리
- 입력값 기본 유효성 검증
- Facade 호출
- API 문서화 (RestDocs)

```kotlin
@RestController
class OrderController(
    private val orderFacade: OrderFacade
) {
    @PostMapping("/orders")
    fun createOrder(@Valid @RequestBody request: OrderRequest): OrderResponse {
        return orderFacade.createOrder(request)
    }
}
```

### Facade 레이어

- 여러 Service 조합
- 트랜잭션 관리
- 요청/응답 DTO 변환
- 비즈니스 로직 오케스트레이션

```kotlin
@Component
class OrderFacade(
    private val orderSaver: OrderSaver,
    private val orderReader: OrderReader,
    private val inventoryUpdater: InventoryUpdater
) {
    @Transactional
    fun createOrder(request: OrderRequest): OrderResponse {
        val order = orderSaver.save(request.toServiceDto())
        inventoryUpdater.decrease(order.items)
        return order.toResponse()
    }
}
```

### Service 레이어

- 핵심 비즈니스 로직 구현
- 도메인 객체 조작
- 단일 책임 원칙 준수
- Repository 호출

**네이밍 규칙**: 기능별로 접미사 사용

| 접미사 | 책임 |
|--------|------|
| `Saver` | 생성 |
| `Reader` | 조회 |
| `Updater` | 수정 |
| `Deleter` | 삭제 |

```kotlin
@Service
class OrderSaver(private val orderRepository: OrderRepository) {
    fun save(dto: OrderServiceDto): Order {
        val order = dto.toEntity()
        return orderRepository.save(order)
    }
}

@Service
class OrderReader(private val orderRepository: OrderRepository) {
    fun getByOrderId(orderId: Long): Order {
        return orderRepository.getByOrderId(orderId)
    }
}
```

### Repository 레이어

- 데이터 영속성 처리
- 엔티티 CRUD 연산
- 쿼리 최적화

### Domain 레이어

- 핵심 비즈니스 모델 정의
- 엔티티 간 관계 설정
- 도메인 규칙 및 제약조건 캡슐화

## 데이터 흐름

| 구간                   | 전달 객체                |
|----------------------|----------------------|
| Controller ↔ Facade  | Request/Response DTO |
| Facade ↔ Service     | Service DTO          |
| Service 내부           | Domain Entity        |
| Service ↔ Repository | Domain Entity        |
| Projections          | DTO로 변환 후 전파         |
