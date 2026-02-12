package com.currenjin.idempotency.payment

data class PaymentRequest(
    val orderId: String,
    val amount: Long,
    val cardNumber: String,
)
