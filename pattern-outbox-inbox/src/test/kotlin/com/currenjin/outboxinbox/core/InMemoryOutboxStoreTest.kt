package com.currenjin.outboxinbox.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class InMemoryOutboxStoreTest {

    private lateinit var store: InMemoryOutboxStore

    @BeforeEach
    fun setUp() {
        store = InMemoryOutboxStore()
    }

    @Test
    fun `레코드를 저장하고 조회할 수 있다`() {
        val record = createRecord("evt-1")
        store.save(record)

        assertThat(store.findAll()).hasSize(1)
        assertThat(store.findAll().first().id).isEqualTo(record.id)
    }

    @Test
    fun `PENDING 상태의 레코드만 조회된다`() {
        val pending = createRecord("evt-1")
        store.save(pending)
        store.save(createRecord("evt-2"))
        store.markSent(store.findAll().last().id, Instant.now())

        val result = store.findPending()

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(pending.id)
    }

    @Test
    fun `레코드를 SENT 상태로 변경할 수 있다`() {
        val record = createRecord("evt-1")
        store.save(record)

        store.markSent(record.id, Instant.now())

        val found = store.findAll().first()
        assertThat(found.status).isEqualTo(OutboxStatus.SENT)
        assertThat(found.sentAt).isNotNull()
    }

    @Test
    fun `레코드를 FAILED 상태로 변경하면 재시도 횟수가 증가한다`() {
        val record = createRecord("evt-1")
        store.save(record)

        store.markFailed(record.id)

        val found = store.findAll().first()
        assertThat(found.status).isEqualTo(OutboxStatus.FAILED)
        assertThat(found.retryCount).isEqualTo(1)
    }

    @Test
    fun `PENDING 레코드는 생성 시간 순으로 정렬된다`() {
        val first = createRecord("evt-1").copy(createdAt = Instant.parse("2024-01-01T00:00:00Z"))
        val second = createRecord("evt-2").copy(createdAt = Instant.parse("2024-01-01T00:00:01Z"))
        store.save(second)
        store.save(first)

        val pending = store.findPending()

        assertThat(pending[0].eventType).isEqualTo("evt-1")
        assertThat(pending[1].eventType).isEqualTo("evt-2")
    }

    private fun createRecord(eventType: String) = OutboxRecord(
        aggregateType = "Order",
        aggregateId = "ORD-001",
        eventType = eventType,
        payload = "test-payload",
    )
}
