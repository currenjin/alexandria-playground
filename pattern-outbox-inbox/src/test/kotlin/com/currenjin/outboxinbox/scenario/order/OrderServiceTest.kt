package com.currenjin.outboxinbox.scenario.order

import com.currenjin.outboxinbox.core.InMemoryOutboxStore
import com.currenjin.outboxinbox.core.OutboxStatus
import com.currenjin.outboxinbox.core.TransactionalOutbox
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {

    private lateinit var orderStore: InMemoryOrderStore
    private lateinit var outboxStore: InMemoryOutboxStore
    private lateinit var orderService: OrderService

    @BeforeEach
    fun setUp() {
        orderStore = InMemoryOrderStore()
        outboxStore = InMemoryOutboxStore()
        orderService = OrderService(orderStore, TransactionalOutbox(outboxStore))
    }

    @Test
    fun `주문 생성 시 주문과 아웃박스 레코드가 함께 저장된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        assertThat(orderStore.findById("ORD-001")).isNotNull
        assertThat(outboxStore.findAll()).hasSize(1)
        assertThat(outboxStore.findAll().first().status).isEqualTo(OutboxStatus.PENDING)
    }

    @Test
    fun `아웃박스 레코드에 올바른 이벤트 타입과 페이로드가 저장된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        val record = outboxStore.findAll().first()
        assertThat(record.eventType).isEqualTo(OrderCreatedEvent.EVENT_TYPE)
        assertThat(record.aggregateType).isEqualTo("Order")
        assertThat(record.aggregateId).isEqualTo("ORD-001")

        val event = OrderCreatedEvent.fromPayload(record.payload)
        assertThat(event.orderId).isEqualTo("ORD-001")
        assertThat(event.productName).isEqualTo("노트북")
        assertThat(event.amount).isEqualTo(1_500_000)
    }

    @Test
    fun `주문 저장 실패 시 아웃박스 레코드도 저장되지 않는다`() {
        val failingStore = object : OrderStore {
            override fun save(order: Order) = throw RuntimeException("DB 오류")
            override fun findById(id: String): Order? = null
            override fun findAll(): List<Order> = emptyList()
        }
        val service = OrderService(failingStore, TransactionalOutbox(outboxStore))

        assertThatThrownBy {
            service.createOrder("ORD-001", "노트북", 1_500_000)
        }.isInstanceOf(RuntimeException::class.java)

        assertThat(outboxStore.findAll()).isEmpty()
    }

    @Test
    fun `여러 주문을 생성하면 각각의 아웃박스 레코드가 생성된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)
        orderService.createOrder("ORD-002", "키보드", 150_000)

        assertThat(orderStore.findAll()).hasSize(2)
        assertThat(outboxStore.findAll()).hasSize(2)
    }
}
