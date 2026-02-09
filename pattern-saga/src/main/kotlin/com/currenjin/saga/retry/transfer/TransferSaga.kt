package com.currenjin.saga.retry.transfer

import com.currenjin.saga.core.RetryPolicy
import com.currenjin.saga.core.SagaOrchestrator
import com.currenjin.saga.core.SagaResult

data class TransferContext(
    val fromAccount: String,
    val toAccount: String,
    val amount: Long,
    var debited: Boolean = false,
    var credited: Boolean = false,
    var compensated: Boolean = false,
    var shouldFailAt: String? = null,
    var retryable: Boolean = true,
    var succeedAfterAttempts: Int = 0,
)

class TransferSaga(retryPolicy: RetryPolicy = RetryPolicy(maxRetries = 3, delayMs = 10)) {
    private val orchestrator = SagaOrchestrator(
        steps = listOf(
            DebitAccountStep(),
            CreditAccountStep(),
        ),
        retryPolicy = retryPolicy,
    )

    val log get() = orchestrator.log

    fun execute(context: TransferContext): SagaResult {
        return orchestrator.execute(context)
    }
}
