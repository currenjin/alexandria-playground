package com.currenjin.idempotency.payment

import java.util.UUID

class PaymentGateway {
    var processedCount = 0
        private set
    var shouldFail: Boolean = false

    fun charge(request: PaymentRequest): PaymentResponse {
        if (shouldFail) {
            throw RuntimeException("결제 게이트웨이 오류")
        }
        processedCount++
        return PaymentResponse(
            transactionId = UUID.randomUUID().toString(),
            orderId = request.orderId,
            amount = request.amount,
            status = "승인",
        )
    }
}
