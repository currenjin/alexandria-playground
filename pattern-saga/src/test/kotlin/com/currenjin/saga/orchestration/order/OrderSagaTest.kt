package com.currenjin.saga.orchestration.order

import com.currenjin.saga.core.SagaResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderSagaTest {

    @Test
    fun `전체 주문 흐름이 성공한다`() {
        val saga = OrderSaga()
        val context = OrderContext(orderId = "ORD-001", amount = 50_000)

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Success::class.java)
        assertThat(context.orderCreated).isTrue()
        assertThat(context.inventoryReserved).isTrue()
        assertThat(context.paymentProcessed).isTrue()
        assertThat(context.orderConfirmed).isTrue()
        assertThat(context.compensationLog).isEmpty()
    }

    @Test
    fun `결제 실패 시 재고 반납과 주문 취소 보상이 수행된다`() {
        val saga = OrderSaga()
        val context = OrderContext(
            orderId = "ORD-002",
            amount = 50_000,
            shouldFailAt = "결제 처리",
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Failure::class.java)
        assertThat((result as SagaResult.Failure).reason).isEqualTo("결제 실패")
        assertThat(context.paymentProcessed).isFalse()
        assertThat(context.inventoryReserved).isFalse()
        assertThat(context.orderCreated).isFalse()
        assertThat(context.compensationLog).containsExactly("재고 예약", "주문 생성")
    }

    @Test
    fun `재고 부족 실패 시 주문 취소 보상이 수행된다`() {
        val saga = OrderSaga()
        val context = OrderContext(
            orderId = "ORD-003",
            amount = 50_000,
            shouldFailAt = "재고 예약",
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Failure::class.java)
        assertThat((result as SagaResult.Failure).reason).isEqualTo("재고 부족")
        assertThat(context.inventoryReserved).isFalse()
        assertThat(context.orderCreated).isFalse()
        assertThat(context.compensationLog).containsExactly("주문 생성")
    }
}
