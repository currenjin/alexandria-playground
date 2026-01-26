# 컨벤션 가이드

## DTO 구조화

- 관련된 DTO들은 Inner Class로 구조화

```kotlin
class OrderDto {
    data class Request(
        val productId: Long,
        val quantity: Int
    )

    data class Response(
        val orderId: Long,
        val status: String
    )

    data class Detail(
        val orderId: Long,
        val items: List<ItemDto>,
        val totalPrice: Int
    )
}
```

## DDL 컨벤션

- 파일명: `V<버전>__<설명>.sql`
- 예시: `V1__create_order_table.sql`, `V2__add_status_column.sql`

## 커뮤니케이션

- AI 어시스턴트는 **한글로 답변**할 것

## 코드 품질

- pre-commit 훅으로 코드 스타일 자동 검사
- 커밋 전 `./gradlew ktlintFormat` 실행 권장
