package com.currenjin.event

import java.math.BigDecimal
import java.util.UUID

data class OrderEvent(
    val orderId: String,
    val userId: String,
    val amount: BigDecimal,
    val eventType: String,
    val traceId: String = UUID.randomUUID().toString(),
)
