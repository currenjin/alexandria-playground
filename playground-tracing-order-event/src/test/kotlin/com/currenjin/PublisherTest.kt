package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PublisherTest {
    private val messageQueue: MessageQueue = MessageQueue()
    private val message = Message(payload = "test", type = "test")

    @Test
    fun publish_test() {
        val publisher = Publisher(messageQueue)
        assertEquals(0, messageQueue.messageSize())

        publisher.publish(message)

        assertEquals(1, messageQueue.messageSize())
    }
}
