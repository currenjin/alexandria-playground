package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class ConfirmOrderStep : SagaStep<OrderContext> {
    override val name = "주문 확정"

    override fun execute(context: OrderContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("주문 확정 실패")
        }
        context.orderConfirmed = true
        return SagaResult.Success("주문 #${context.orderId} 확정됨")
    }

    override fun compensate(context: OrderContext): SagaResult {
        context.orderConfirmed = false
        context.compensationLog.add(name)
        return SagaResult.Success("주문 #${context.orderId} 확정 취소됨")
    }
}
