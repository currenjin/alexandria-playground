package com.currenjin.event

import java.math.BigDecimal

data class OrderEvent(
    val orderId: String,
    val userId: String,
    val amount: BigDecimal,
    val eventType: String,
)
