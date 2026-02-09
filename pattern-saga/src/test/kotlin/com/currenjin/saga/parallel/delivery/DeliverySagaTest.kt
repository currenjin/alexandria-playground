package com.currenjin.saga.parallel.delivery

import com.currenjin.saga.core.SagaResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DeliverySagaTest {

    @Test
    fun `전체 배달 흐름이 성공한다`() {
        val saga = DeliverySaga()
        val context = DeliveryContext(orderId = "DLV-001")

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Success::class.java)
        assertThat(context.orderPlaced).isTrue()
        assertThat(context.foodPrepared).isTrue()
        assertThat(context.driverAssigned).isTrue()
        assertThat(context.delivered).isTrue()
        assertThat(context.compensationLog).isEmpty()
    }

    @Test
    fun `드라이버 배정 실패 시 음식 준비 취소와 주문 취소 보상이 수행된다`() {
        val saga = DeliverySaga()
        val context = DeliveryContext(
            orderId = "DLV-002",
            shouldFailAt = "드라이버 배정",
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Failure::class.java)
        assertThat((result as SagaResult.Failure).reason).isEqualTo("드라이버 배정 실패")
        assertThat(context.driverAssigned).isFalse()
        assertThat(context.foodPrepared).isFalse()
        assertThat(context.orderPlaced).isFalse()
        assertThat(context.compensationLog).contains("음식 준비", "주문 접수")
    }

    @Test
    fun `배달 실패 시 병렬 스텝 보상과 주문 취소 보상이 수행된다`() {
        val saga = DeliverySaga()
        val context = DeliveryContext(
            orderId = "DLV-003",
            shouldFailAt = "배달",
        )

        val result = saga.execute(context)

        assertThat(result).isInstanceOf(SagaResult.Failure::class.java)
        assertThat((result as SagaResult.Failure).reason).isEqualTo("배달 실패")
        assertThat(context.delivered).isFalse()
        assertThat(context.foodPrepared).isFalse()
        assertThat(context.driverAssigned).isFalse()
        assertThat(context.orderPlaced).isFalse()
        assertThat(context.compensationLog).contains("드라이버 배정", "음식 준비", "주문 접수")
    }
}
