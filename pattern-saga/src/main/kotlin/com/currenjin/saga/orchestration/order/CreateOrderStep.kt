package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class CreateOrderStep : SagaStep<OrderContext> {
    override val name = "주문 생성"

    override fun execute(context: OrderContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("주문 생성 실패")
        }
        context.orderCreated = true
        return SagaResult.Success("주문 #${context.orderId} 생성됨")
    }

    override fun compensate(context: OrderContext): SagaResult {
        context.orderCreated = false
        context.compensationLog.add(name)
        return SagaResult.Success("주문 #${context.orderId} 취소됨")
    }
}
