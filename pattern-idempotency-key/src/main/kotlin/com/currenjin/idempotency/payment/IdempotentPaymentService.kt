package com.currenjin.idempotency.payment

import com.currenjin.idempotency.core.IdempotencyExecutor
import com.currenjin.idempotency.core.IdempotencyKey
import com.currenjin.idempotency.core.IdempotencyResult
import com.currenjin.idempotency.core.InMemoryIdempotencyStore

class IdempotentPaymentService(
    private val gateway: PaymentGateway = PaymentGateway(),
) {
    private val store = InMemoryIdempotencyStore<PaymentResponse>()
    private val executor = IdempotencyExecutor(store)

    val log get() = executor.log

    fun processPayment(idempotencyKey: IdempotencyKey, request: PaymentRequest): IdempotencyResult<PaymentResponse> {
        return executor.execute(idempotencyKey) {
            gateway.charge(request)
        }
    }

    val processedCount get() = gateway.processedCount
}
