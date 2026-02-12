package com.currenjin.idempotency.payment

data class PaymentResponse(
    val transactionId: String,
    val orderId: String,
    val amount: Long,
    val status: String,
)
