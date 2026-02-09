package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class ProcessPaymentStep : SagaStep<OrderContext> {
    override val name = "결제 처리"

    override fun execute(context: OrderContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("결제 실패")
        }
        context.paymentProcessed = true
        return SagaResult.Success("결제 완료: ${context.amount}원")
    }

    override fun compensate(context: OrderContext): SagaResult {
        context.paymentProcessed = false
        context.compensationLog.add(name)
        return SagaResult.Success("환불 완료: ${context.amount}원")
    }
}
