package com.currenjin.saga.retry.transfer

import com.currenjin.saga.core.SagaResult
import com.currenjin.saga.core.SagaStep

class CreditAccountStep : SagaStep<TransferContext> {
    override val name = "입금"

    private var attemptCount = 0

    override fun execute(context: TransferContext): SagaResult {
        attemptCount++

        if (context.shouldFailAt == name) {
            if (context.succeedAfterAttempts > 0 && attemptCount >= context.succeedAfterAttempts) {
                context.credited = true
                return SagaResult.Success("${context.toAccount}에 ${context.amount}원 입금 완료")
            }
            return SagaResult.Failure("입금 실패 (시도 $attemptCount)", retryable = context.retryable)
        }

        context.credited = true
        return SagaResult.Success("${context.toAccount}에 ${context.amount}원 입금 완료")
    }

    override fun compensate(context: TransferContext): SagaResult {
        context.credited = false
        context.compensated = true
        return SagaResult.Success("${context.toAccount}에서 ${context.amount}원 입금 취소")
    }
}
