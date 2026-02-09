package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class DeliverStep : SagaStep<DeliveryContext> {
    override val name = "배달"

    override fun execute(context: DeliveryContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("배달 실패")
        }
        context.delivered = true
        return SagaResult.Success("배달 완료")
    }

    override fun compensate(context: DeliveryContext): SagaResult {
        context.delivered = false
        context.compensationLog.add(name)
        return SagaResult.Success("배달 취소")
    }
}
