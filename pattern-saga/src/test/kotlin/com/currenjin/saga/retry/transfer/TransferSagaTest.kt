package com.currenjin.saga.retry.transfer

import com.currenjin.saga.core.RetryPolicy
import com.currenjin.saga.core.SagaResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TransferSagaTest {

    @Test
    fun `전체 계좌 이체가 성공한다`() {
        val saga = TransferSaga()
        val context = TransferContext(
            fromAccount = "A-001",
            toAccount = "B-001",
            amount = 100_000,
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Success::class.java)
        assertThat(context.debited).isTrue()
        assertThat(context.credited).isTrue()
        assertThat(context.compensated).isFalse()
    }

    @Test
    fun `일시적 입금 실패 후 재시도로 성공한다`() {
        val saga = TransferSaga(RetryPolicy(maxRetries = 3, delayMs = 10))
        val context = TransferContext(
            fromAccount = "A-002",
            toAccount = "B-002",
            amount = 50_000,
            shouldFailAt = "입금",
            retryable = true,
            succeedAfterAttempts = 3,
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Success::class.java)
        assertThat(context.debited).isTrue()
        assertThat(context.credited).isTrue()
        assertThat(context.compensated).isFalse()
        assertThat(saga.log).anyMatch { it.contains("재시도") }
    }

    @Test
    fun `재시도 모두 실패 시 출금 보상이 수행된다`() {
        val saga = TransferSaga(RetryPolicy(maxRetries = 3, delayMs = 10))
        val context = TransferContext(
            fromAccount = "A-003",
            toAccount = "B-003",
            amount = 200_000,
            shouldFailAt = "입금",
            retryable = true,
            succeedAfterAttempts = 0,
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Failure::class.java)
        assertThat(context.debited).isFalse()
        assertThat(context.credited).isFalse()
        assertThat(context.compensated).isTrue()
    }
}
