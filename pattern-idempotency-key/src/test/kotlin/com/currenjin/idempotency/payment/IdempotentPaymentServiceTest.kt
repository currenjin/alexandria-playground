package com.currenjin.idempotency.payment

import com.currenjin.idempotency.core.IdempotencyKey
import com.currenjin.idempotency.core.IdempotencyResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdempotentPaymentServiceTest {

    private lateinit var service: IdempotentPaymentService

    @BeforeEach
    fun setUp() {
        service = IdempotentPaymentService()
    }

    @Test
    fun `결제가 정상적으로 처리된다`() {
        val key = IdempotencyKey("pay-001")
        val request = PaymentRequest(orderId = "ORD-001", amount = 50_000, cardNumber = "1234")

        val result = service.processPayment(key, request)

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        val response = (result as IdempotencyResult.Executed).response
        assertThat(response.orderId).isEqualTo("ORD-001")
        assertThat(response.amount).isEqualTo(50_000)
        assertThat(response.status).isEqualTo("승인")
        assertThat(service.processedCount).isEqualTo(1)
    }

    @Test
    fun `동일한 멱등성 키로 중복 결제를 방지한다`() {
        val key = IdempotencyKey("pay-002")
        val request = PaymentRequest(orderId = "ORD-002", amount = 30_000, cardNumber = "5678")

        val first = service.processPayment(key, request)
        val second = service.processPayment(key, request)

        assertThat(first).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat(second).isInstanceOf(IdempotencyResult.Replayed::class.java)
        assertThat((first as IdempotencyResult.Executed).response.transactionId)
            .isEqualTo((second as IdempotencyResult.Replayed).response.transactionId)
        assertThat(service.processedCount).isEqualTo(1)
    }

    @Test
    fun `서로 다른 멱등성 키는 각각 결제된다`() {
        val request = PaymentRequest(orderId = "ORD-003", amount = 10_000, cardNumber = "9999")

        service.processPayment(IdempotencyKey("pay-003a"), request)
        service.processPayment(IdempotencyKey("pay-003b"), request)

        assertThat(service.processedCount).isEqualTo(2)
    }

    @Test
    fun `결제 실패 후 동일 키로 재시도하면 다시 결제를 시도한다`() {
        val gateway = PaymentGateway()
        val service = IdempotentPaymentService(gateway)
        val key = IdempotencyKey("pay-004")
        val request = PaymentRequest(orderId = "ORD-004", amount = 20_000, cardNumber = "1111")

        gateway.shouldFail = true
        try {
            service.processPayment(key, request)
        } catch (_: RuntimeException) {
        }

        gateway.shouldFail = false
        val result = service.processPayment(key, request)

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat(gateway.processedCount).isEqualTo(1)
    }
}
