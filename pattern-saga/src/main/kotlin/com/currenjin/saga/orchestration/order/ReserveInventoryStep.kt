package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class ReserveInventoryStep : SagaStep<OrderContext> {
    override val name = "재고 예약"

    override fun execute(context: OrderContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("재고 부족")
        }
        context.inventoryReserved = true
        return SagaResult.Success("재고 예약 완료")
    }

    override fun compensate(context: OrderContext): SagaResult {
        context.inventoryReserved = false
        context.compensationLog.add(name)
        return SagaResult.Success("재고 반납 완료")
    }
}
