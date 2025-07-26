package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TracingMessageTest {
    @Test
    fun auto_generate_trace_id_in_message() {
        val message = Message(payload = "payload", type = "type")

        assertTrue(message.traceId.isNotBlank())
    }
}
