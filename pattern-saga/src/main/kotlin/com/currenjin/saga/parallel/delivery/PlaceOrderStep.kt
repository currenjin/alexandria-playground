package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class PlaceOrderStep : SagaStep<DeliveryContext> {
    override val name = "주문 접수"

    override fun execute(context: DeliveryContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("주문 접수 실패")
        }
        context.orderPlaced = true
        return SagaResult.Success("주문 #${context.orderId} 접수 완료")
    }

    override fun compensate(context: DeliveryContext): SagaResult {
        context.orderPlaced = false
        context.compensationLog.add(name)
        return SagaResult.Success("주문 #${context.orderId} 접수 취소")
    }
}
