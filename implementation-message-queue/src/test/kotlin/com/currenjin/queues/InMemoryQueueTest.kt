package com.currenjin.queues

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryQueueTest {
    private lateinit var queue: InMemoryQueue

    @BeforeEach
    fun setup() {
        queue = InMemoryQueue("test-queue")
    }

    @Test
    fun should_create_empty_queue() {
        assertEquals("test-queue", queue.name)
        assertEquals(0, queue.size())
        assertTrue(queue.isEmpty())
    }

    @Test
    fun should_publish_message_to_queue() {
        queue.publish("Test message")

        assertEquals(1, queue.size())
        assertFalse(queue.isEmpty())
    }

    @Test
    fun should_consume_message_from_queue() {
        val messagePayload = "Test message"
        queue.publish(messagePayload)

        val message = queue.consume()

        assertNotNull(message)
        assertEquals(messagePayload, message?.payload)
        assertEquals(0, queue.size())
        assertTrue(queue.isEmpty())
    }

    @Test
    fun should_return_null_when_consuming_from_empty_queue() {
        val message = queue.consume()

        assertNull(message)
    }

    @Test
    fun should_consume_messages_in_FIFO_order() {
        val message1 = "First message"
        val message2 = "Second message"
        val message3 = "Third message"

        queue.publish(message1)
        queue.publish(message2)
        queue.publish(message3)

        assertEquals(message1, queue.consume()?.payload)
        assertEquals(message2, queue.consume()?.payload)
        assertEquals(message3, queue.consume()?.payload)
        assertNull(queue.consume())
    }

    @Test
    fun should_handle_multiple_publish_and_consume_operations() {
        queue.publish("Message 1")
        queue.publish("Message 2")

        val message1 = queue.consume()
        assertEquals("Message 1", message1?.payload)
        assertEquals(1, queue.size())

        queue.publish("Message 3")
        assertEquals(2, queue.size())

        assertEquals("Message 2", queue.consume()?.payload)
        assertEquals("Message 3", queue.consume()?.payload)
        assertTrue(queue.isEmpty())
    }
}
