package com.currenjin.idempotency.message

import com.currenjin.idempotency.core.IdempotencyResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IdempotentMessageProcessorTest {

    private lateinit var processor: IdempotentMessageProcessor

    @BeforeEach
    fun setUp() {
        processor = IdempotentMessageProcessor()
    }

    @Test
    fun `메시지가 정상적으로 처리된다`() {
        val message = Message(messageId = "msg-001", topic = "주문", payload = "주문 데이터")

        val result = processor.process(message)

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        val response = (result as IdempotencyResult.Executed).response
        assertThat(response.messageId).isEqualTo("msg-001")
        assertThat(response.processed).isTrue()
        assertThat(processor.processedCount).isEqualTo(1)
    }

    @Test
    fun `동일한 메시지 ID로 중복 처리를 방지한다`() {
        val message = Message(messageId = "msg-002", topic = "결제", payload = "결제 데이터")

        processor.process(message)
        val second = processor.process(message)

        assertThat(second).isInstanceOf(IdempotencyResult.Replayed::class.java)
        assertThat(processor.processedCount).isEqualTo(1)
    }

    @Test
    fun `서로 다른 메시지 ID는 각각 처리된다`() {
        val message1 = Message(messageId = "msg-003", topic = "알림", payload = "알림 1")
        val message2 = Message(messageId = "msg-004", topic = "알림", payload = "알림 2")

        processor.process(message1)
        processor.process(message2)

        assertThat(processor.processedCount).isEqualTo(2)
    }

    @Test
    fun `처리 실패 후 동일 메시지를 재처리할 수 있다`() {
        val handler = MessageHandler()
        val processor = IdempotentMessageProcessor(handler)
        val message = Message(messageId = "msg-005", topic = "이벤트", payload = "이벤트 데이터")

        handler.shouldFail = true
        try {
            processor.process(message)
        } catch (_: RuntimeException) {
        }

        handler.shouldFail = false
        val result = processor.process(message)

        assertThat(result).isInstanceOf(IdempotencyResult.Executed::class.java)
        assertThat(handler.processedMessages).hasSize(1)
    }

    @Test
    fun `메시지 ID로부터 멱등성 키가 자동 생성된다`() {
        val message = Message(messageId = "msg-006", topic = "테스트", payload = "데이터")

        processor.process(message)
        val result = processor.process(message)

        assertThat(result).isInstanceOf(IdempotencyResult.Replayed::class.java)
        assertThat(processor.log).anyMatch { it.contains("message:msg-006") }
    }
}
