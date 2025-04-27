package com.currenjin.messages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MessageTest {
    @Test
    fun should_create_message_with_required_fields() {
        val payload = "test message"

        val message = Message(payload = payload)

        assertNotNull(message.id)
        assertEquals(payload, message.payload)
        assertTrue(message.timestamp > 0)
        assertTrue(message.headers.isEmpty())
    }

    @Test
    fun should_create_message_with_custom_headers() {
        val headers = mapOf("priority" to "high", "source" to "test")
        val payload = "test message"

        val message = Message(payload = payload, headers = headers)

        assertEquals(headers, message.headers)
    }

    @Test
    fun should_generate_unique_ids_for_different_messages() {
        val message1 = Message(payload = "first message")
        val message2 = Message(payload = "second message")

        assertNotEquals(message1.id, message2.id)
    }
}
