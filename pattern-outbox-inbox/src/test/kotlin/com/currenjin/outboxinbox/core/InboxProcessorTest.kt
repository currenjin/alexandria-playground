package com.currenjin.outboxinbox.core

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InboxProcessorTest {

    private lateinit var inboxStore: InMemoryInboxStore
    private lateinit var processor: InboxProcessor

    @BeforeEach
    fun setUp() {
        inboxStore = InMemoryInboxStore()
        processor = InboxProcessor(inboxStore)
    }

    @Test
    fun `새로운 메시지를 정상적으로 처리한다`() {
        val message = createMessage("msg-001")
        var handled = false

        processor.process(message) { handled = true }

        assertThat(handled).isTrue()
        assertThat(inboxStore.find("msg-001")!!.status).isEqualTo(InboxStatus.COMPLETED)
    }

    @Test
    fun `이미 처리 완료된 메시지는 중복 스킵한다`() {
        val message = createMessage("msg-001")
        var handlerCallCount = 0

        processor.process(message) { handlerCallCount++ }
        processor.process(message) { handlerCallCount++ }

        assertThat(handlerCallCount).isEqualTo(1)
        assertThat(processor.log).anyMatch { it.contains("중복 메시지 스킵") }
    }

    @Test
    fun `처리 중 예외가 발생하면 FAILED로 기록한다`() {
        val message = createMessage("msg-001")

        assertThatThrownBy {
            processor.process(message) { throw RuntimeException("처리 오류") }
        }.isInstanceOf(RuntimeException::class.java)

        assertThat(inboxStore.find("msg-001")!!.status).isEqualTo(InboxStatus.FAILED)
    }

    @Test
    fun `FAILED 상태의 메시지는 재처리를 허용한다`() {
        val message = createMessage("msg-001")
        var shouldFail = true

        assertThatThrownBy {
            processor.process(message) {
                if (shouldFail) throw RuntimeException("실패")
            }
        }.isInstanceOf(RuntimeException::class.java)

        shouldFail = false
        processor.process(message) {}

        assertThat(inboxStore.find("msg-001")!!.status).isEqualTo(InboxStatus.COMPLETED)
        assertThat(processor.log).anyMatch { it.contains("실패한 메시지 재처리") }
    }

    @Test
    fun `동일 메시지를 여러 번 전달해도 핸들러는 한 번만 실행된다`() {
        val message = createMessage("msg-001")
        var handlerCallCount = 0

        repeat(5) {
            processor.process(message) { handlerCallCount++ }
        }

        assertThat(handlerCallCount).isEqualTo(1)
    }

    private fun createMessage(id: String) = Message(
        id = id,
        eventType = "TestEvent",
        payload = "test-payload",
        aggregateType = "Test",
        aggregateId = "T-001",
    )
}
