package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class PrepareFoodStep : SagaStep<DeliveryContext> {
    override val name = "음식 준비"

    override fun execute(context: DeliveryContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("음식 준비 실패")
        }
        context.foodPrepared = true
        return SagaResult.Success("음식 준비 완료")
    }

    override fun compensate(context: DeliveryContext): SagaResult {
        context.foodPrepared = false
        context.compensationLog.add(name)
        return SagaResult.Success("음식 준비 취소")
    }
}
