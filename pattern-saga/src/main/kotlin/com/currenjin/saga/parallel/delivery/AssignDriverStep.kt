package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class AssignDriverStep : SagaStep<DeliveryContext> {
    override val name = "드라이버 배정"

    override fun execute(context: DeliveryContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("드라이버 배정 실패")
        }
        context.driverAssigned = true
        return SagaResult.Success("드라이버 배정 완료")
    }

    override fun compensate(context: DeliveryContext): SagaResult {
        context.driverAssigned = false
        context.compensationLog.add(name)
        return SagaResult.Success("드라이버 배정 취소")
    }
}
