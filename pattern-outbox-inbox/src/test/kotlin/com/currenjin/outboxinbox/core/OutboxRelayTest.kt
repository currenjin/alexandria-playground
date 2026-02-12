package com.currenjin.outboxinbox.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OutboxRelayTest {

    private lateinit var outboxStore: InMemoryOutboxStore
    private lateinit var messageBroker: InMemoryMessageBroker
    private lateinit var relay: OutboxRelay

    @BeforeEach
    fun setUp() {
        outboxStore = InMemoryOutboxStore()
        messageBroker = InMemoryMessageBroker()
        relay = OutboxRelay(outboxStore, messageBroker)
    }

    @Test
    fun `PENDING 이벤트를 메시지 브로커에 발행하고 SENT로 변경한다`() {
        val record = createRecord()
        outboxStore.save(record)

        val published = relay.pollAndPublish()

        assertThat(published).isEqualTo(1)
        assertThat(messageBroker.publishedMessages).hasSize(1)
        assertThat(outboxStore.findAll().first().status).isEqualTo(OutboxStatus.SENT)
    }

    @Test
    fun `발행할 이벤트가 없으면 0을 반환한다`() {
        val published = relay.pollAndPublish()

        assertThat(published).isEqualTo(0)
        assertThat(messageBroker.publishedMessages).isEmpty()
    }

    @Test
    fun `이미 SENT된 이벤트는 다시 발행하지 않는다`() {
        val record = createRecord()
        outboxStore.save(record)

        relay.pollAndPublish()
        val secondPublished = relay.pollAndPublish()

        assertThat(secondPublished).isEqualTo(0)
        assertThat(messageBroker.publishedMessages).hasSize(1)
    }

    @Test
    fun `여러 PENDING 이벤트를 순서대로 발행한다`() {
        outboxStore.save(createRecord("ORD-001"))
        outboxStore.save(createRecord("ORD-002"))
        outboxStore.save(createRecord("ORD-003"))

        val published = relay.pollAndPublish()

        assertThat(published).isEqualTo(3)
        assertThat(messageBroker.publishedMessages).hasSize(3)
        assertThat(outboxStore.findPending()).isEmpty()
    }

    @Test
    fun `발행된 메시지에 올바른 정보가 포함된다`() {
        val record = createRecord("ORD-001")
        outboxStore.save(record)

        relay.pollAndPublish()

        val (topic, message) = messageBroker.publishedMessages.first()
        assertThat(topic).isEqualTo("OrderCreatedEvent")
        assertThat(message.id).isEqualTo(record.id)
        assertThat(message.aggregateType).isEqualTo("Order")
        assertThat(message.aggregateId).isEqualTo("ORD-001")
        assertThat(message.payload).isEqualTo("test-payload")
    }

    private fun createRecord(aggregateId: String = "ORD-001") = OutboxRecord(
        aggregateType = "Order",
        aggregateId = aggregateId,
        eventType = "OrderCreatedEvent",
        payload = "test-payload",
    )
}
