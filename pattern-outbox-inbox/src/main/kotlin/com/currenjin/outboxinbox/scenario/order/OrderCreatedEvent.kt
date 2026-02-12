package com.currenjin.outboxinbox.scenario.order

data class OrderCreatedEvent(
    val orderId: String,
    val productName: String,
    val amount: Long,
) {
    fun toPayload(): String = "$orderId|$productName|$amount"

    companion object {
        const val EVENT_TYPE = "OrderCreatedEvent"

        fun fromPayload(payload: String): OrderCreatedEvent {
            val parts = payload.split("|")
            return OrderCreatedEvent(
                orderId = parts[0],
                productName = parts[1],
                amount = parts[2].toLong(),
            )
        }
    }
}
