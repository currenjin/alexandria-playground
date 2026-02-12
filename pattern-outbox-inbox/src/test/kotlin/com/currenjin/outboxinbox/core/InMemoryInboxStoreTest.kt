package com.currenjin.outboxinbox.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InMemoryInboxStoreTest {

    private lateinit var store: InMemoryInboxStore

    @BeforeEach
    fun setUp() {
        store = InMemoryInboxStore()
    }

    @Test
    fun `레코드를 저장하고 존재 여부를 확인할 수 있다`() {
        val record = InboxRecord(messageId = "msg-001", eventType = "TestEvent", status = InboxStatus.COMPLETED)
        store.save(record)

        assertThat(store.exists("msg-001")).isTrue()
    }

    @Test
    fun `존재하지 않는 메시지 ID는 false를 반환한다`() {
        assertThat(store.exists("non-existent")).isFalse()
    }

    @Test
    fun `레코드를 COMPLETED 상태로 변경할 수 있다`() {
        val record = InboxRecord(messageId = "msg-001", eventType = "TestEvent", status = InboxStatus.PROCESSING)
        store.save(record)

        store.markCompleted("msg-001")

        val found = store.find("msg-001")
        assertThat(found!!.status).isEqualTo(InboxStatus.COMPLETED)
        assertThat(found.processedAt).isNotNull()
    }

    @Test
    fun `레코드를 FAILED 상태로 변경하면 에러 메시지가 저장된다`() {
        val record = InboxRecord(messageId = "msg-001", eventType = "TestEvent", status = InboxStatus.PROCESSING)
        store.save(record)

        store.markFailed("msg-001", "처리 실패")

        val found = store.find("msg-001")
        assertThat(found!!.status).isEqualTo(InboxStatus.FAILED)
        assertThat(found.errorMessage).isEqualTo("처리 실패")
    }
}
