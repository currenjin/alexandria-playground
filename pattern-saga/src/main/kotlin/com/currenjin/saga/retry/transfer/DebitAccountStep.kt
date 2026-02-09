package com.currenjin.saga.retry.transfer

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class DebitAccountStep : SagaStep<TransferContext> {
    override val name = "출금"

    override fun execute(context: TransferContext): SagaResult {
        if (context.shouldFailAt == name) {
            return SagaResult.Failure("출금 실패", retryable = context.retryable)
        }
        context.debited = true
        return SagaResult.Success("${context.fromAccount}에서 ${context.amount}원 출금 완료")
    }

    override fun compensate(context: TransferContext): SagaResult {
        context.debited = false
        context.compensated = true
        return SagaResult.Success("${context.fromAccount}에 ${context.amount}원 환불 완료")
    }
}
