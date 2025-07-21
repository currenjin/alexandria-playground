package com.currenjin.event

import java.math.BigDecimal

data class PaymentEvent(
    val orderId: String,
    val amount: BigDecimal,
    val status: String,
)
