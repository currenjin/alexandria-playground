package com.currenjin.outboxinbox.scenario.order

import com.currenjin.outboxinbox.core.InMemoryInboxStore
import com.currenjin.outboxinbox.core.InMemoryMessageBroker
import com.currenjin.outboxinbox.core.InboxProcessor
import com.currenjin.outboxinbox.core.Message
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PaymentServiceTest {

    private lateinit var paymentStore: InMemoryPaymentStore
    private lateinit var inboxStore: InMemoryInboxStore
    private lateinit var messageBroker: InMemoryMessageBroker
    private lateinit var paymentService: PaymentService

    @BeforeEach
    fun setUp() {
        paymentStore = InMemoryPaymentStore()
        inboxStore = InMemoryInboxStore()
        messageBroker = InMemoryMessageBroker()
        paymentService = PaymentService(paymentStore, InboxProcessor(inboxStore), messageBroker)
    }

    @Test
    fun `메시지를 수신하면 결제가 생성된다`() {
        val message = createMessage("msg-001", "ORD-001", 50_000)

        paymentService.processMessage(message)

        assertThat(paymentStore.findByOrderId("ORD-001")).isNotNull
        assertThat(paymentStore.findByOrderId("ORD-001")!!.amount).isEqualTo(50_000)
        assertThat(paymentService.processedCount).isEqualTo(1)
    }

    @Test
    fun `동일 메시지를 두 번 수신해도 결제는 한 번만 생성된다`() {
        val message = createMessage("msg-001", "ORD-001", 50_000)

        paymentService.processMessage(message)
        paymentService.processMessage(message)

        assertThat(paymentStore.findAll()).hasSize(1)
        assertThat(paymentService.processedCount).isEqualTo(1)
    }

    @Test
    fun `서로 다른 메시지는 각각 결제가 생성된다`() {
        paymentService.processMessage(createMessage("msg-001", "ORD-001", 50_000))
        paymentService.processMessage(createMessage("msg-002", "ORD-002", 30_000))

        assertThat(paymentStore.findAll()).hasSize(2)
        assertThat(paymentService.processedCount).isEqualTo(2)
    }

    @Test
    fun `구독 후 브로커에서 메시지를 수신하면 결제가 생성된다`() {
        paymentService.subscribe()

        val event = OrderCreatedEvent("ORD-001", "노트북", 1_500_000)
        val message = Message(
            id = "msg-001",
            eventType = OrderCreatedEvent.EVENT_TYPE,
            payload = event.toPayload(),
            aggregateType = "Order",
            aggregateId = "ORD-001",
        )
        messageBroker.publish(OrderCreatedEvent.EVENT_TYPE, message)

        assertThat(paymentStore.findByOrderId("ORD-001")).isNotNull
        assertThat(paymentService.processedCount).isEqualTo(1)
    }

    private fun createMessage(id: String, orderId: String, amount: Long): Message {
        val event = OrderCreatedEvent(orderId, "상품", amount)
        return Message(
            id = id,
            eventType = OrderCreatedEvent.EVENT_TYPE,
            payload = event.toPayload(),
            aggregateType = "Order",
            aggregateId = orderId,
        )
    }
}
