package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TracingMessageTest {
    private val messageQueue = MessageQueue()

    private val publisher = Publisher(messageQueue = messageQueue)

    @Test
    fun auto_generate_trace_id_in_message() {
        val message = Message(payload = "payload", type = "type")

        assertTrue(message.traceId.isNotBlank())
    }

    @Test
    fun trace_id_for_subscription() {
        val message = Message(payload = SomeEvent(), type = SomeEvent::class.java.simpleName)
        val subscriber = Subscriber(messageQueue = messageQueue, targetEvent = SomeEvent::class)

        publisher.publish(message)

        subscriber.subscribe { actual -> assertEquals(message.traceId, actual.traceId) }
    }

    class SomeEvent
}
