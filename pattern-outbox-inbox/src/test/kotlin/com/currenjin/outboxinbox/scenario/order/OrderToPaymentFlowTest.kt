package com.currenjin.outboxinbox.scenario.order

import com.currenjin.outboxinbox.core.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderToPaymentFlowTest {

    private lateinit var orderStore: InMemoryOrderStore
    private lateinit var outboxStore: InMemoryOutboxStore
    private lateinit var messageBroker: InMemoryMessageBroker
    private lateinit var paymentStore: InMemoryPaymentStore
    private lateinit var inboxStore: InMemoryInboxStore

    private lateinit var orderService: OrderService
    private lateinit var relay: OutboxRelay
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        orderStore = InMemoryOrderStore()
        outboxStore = InMemoryOutboxStore()
        messageBroker = InMemoryMessageBroker()
        paymentStore = InMemoryPaymentStore()
        inboxStore = InMemoryInboxStore()

        orderService = OrderService(orderStore, TransactionalOutbox(outboxStore))
        relay = OutboxRelay(outboxStore, messageBroker)
        paymentService = PaymentService(paymentStore, InboxProcessor(inboxStore), messageBroker)
        paymentService.subscribe()
    }

    @Test
    fun `주문 생성부터 결제 처리까지 전체 흐름이 동작한다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        relay.pollAndPublish()

        assertThat(orderStore.findById("ORD-001")).isNotNull
        assertThat(paymentStore.findByOrderId("ORD-001")).isNotNull
        assertThat(paymentStore.findByOrderId("ORD-001")!!.amount).isEqualTo(1_500_000)
    }

    @Test
    fun `Relay를 두 번 실행해도 이벤트가 중복 발행되지 않는다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        val firstPublished = relay.pollAndPublish()
        val secondPublished = relay.pollAndPublish()

        assertThat(firstPublished).isEqualTo(1)
        assertThat(secondPublished).isEqualTo(0)
        assertThat(messageBroker.publishedMessages).hasSize(1)
    }

    @Test
    fun `동일 이벤트가 중복 전달되어도 결제는 한 번만 생성된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        relay.pollAndPublish()

        val duplicateMessage = messageBroker.publishedMessages.first().second
        paymentService.processMessage(duplicateMessage)

        assertThat(paymentStore.findAll()).hasSize(1)
        assertThat(paymentService.processedCount).isEqualTo(1)
    }

    @Test
    fun `여러 주문이 순서대로 처리된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)
        orderService.createOrder("ORD-002", "키보드", 150_000)
        orderService.createOrder("ORD-003", "마우스", 80_000)

        relay.pollAndPublish()

        assertThat(paymentStore.findAll()).hasSize(3)
        assertThat(paymentStore.findByOrderId("ORD-001")!!.amount).isEqualTo(1_500_000)
        assertThat(paymentStore.findByOrderId("ORD-002")!!.amount).isEqualTo(150_000)
        assertThat(paymentStore.findByOrderId("ORD-003")!!.amount).isEqualTo(80_000)
    }

    @Test
    fun `Relay 실행 전에는 결제가 생성되지 않는다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        assertThat(paymentStore.findAll()).isEmpty()
        assertThat(outboxStore.findPending()).hasSize(1)

        relay.pollAndPublish()

        assertThat(paymentStore.findAll()).hasSize(1)
        assertThat(outboxStore.findPending()).isEmpty()
    }

    @Test
    fun `Outbox는 SENT 상태로, Inbox는 COMPLETED 상태로 최종 전이된다`() {
        orderService.createOrder("ORD-001", "노트북", 1_500_000)

        relay.pollAndPublish()

        assertThat(outboxStore.findAll().first().status).isEqualTo(OutboxStatus.SENT)
        assertThat(inboxStore.findAll().first().status).isEqualTo(InboxStatus.COMPLETED)
    }
}
