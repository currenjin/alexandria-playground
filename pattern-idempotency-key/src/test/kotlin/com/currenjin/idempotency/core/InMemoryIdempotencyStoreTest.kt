package com.currenjin.idempotency.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class InMemoryIdempotencyStoreTest {

    private lateinit var clock: Clock
    private lateinit var store: InMemoryIdempotencyStore<String>

    @BeforeEach
    fun setUp() {
        clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("UTC"))
        store = InMemoryIdempotencyStore(clock)
    }

    @Test
    fun `새로운 키로 저장하면 성공한다`() {
        val record = createRecord("key-1", RecordStatus.COMPLETED, "응답")

        val saved = store.save(record)

        assertThat(saved).isTrue()
        assertThat(store.find(IdempotencyKey("key-1"))).isNotNull
    }

    @Test
    fun `동일한 키로 중복 저장하면 실패한다`() {
        val record = createRecord("key-1", RecordStatus.COMPLETED, "응답")
        store.save(record)

        val duplicate = store.save(record)

        assertThat(duplicate).isFalse()
    }

    @Test
    fun `저장된 레코드를 조회할 수 있다`() {
        val record = createRecord("key-1", RecordStatus.COMPLETED, "응답 데이터")
        store.save(record)

        val found = store.find(IdempotencyKey("key-1"))

        assertThat(found).isNotNull
        assertThat(found!!.response).isEqualTo("응답 데이터")
        assertThat(found.status).isEqualTo(RecordStatus.COMPLETED)
    }

    @Test
    fun `존재하지 않는 키를 조회하면 null을 반환한다`() {
        val found = store.find(IdempotencyKey("non-existent"))

        assertThat(found).isNull()
    }

    @Test
    fun `만료된 레코드는 조회되지 않는다`() {
        val expiredRecord = IdempotencyRecord(
            key = IdempotencyKey("expired-key"),
            status = RecordStatus.COMPLETED,
            response = "응답",
            expiresAt = Instant.parse("2023-12-31T23:59:59Z"),
        )
        store.save(expiredRecord)

        assertThat(store.find(IdempotencyKey("expired-key"))).isNull()
    }

    @Test
    fun `레코드를 업데이트할 수 있다`() {
        val record = createRecord("key-1", RecordStatus.PROCESSING, null)
        store.save(record)

        val updated = record.copy(status = RecordStatus.COMPLETED, response = "완료")
        store.update(updated)

        val found = store.find(IdempotencyKey("key-1"))
        assertThat(found!!.status).isEqualTo(RecordStatus.COMPLETED)
        assertThat(found.response).isEqualTo("완료")
    }

    @Test
    fun `레코드를 삭제할 수 있다`() {
        val record = createRecord("key-1", RecordStatus.COMPLETED, "응답")
        store.save(record)

        store.remove(IdempotencyKey("key-1"))

        assertThat(store.find(IdempotencyKey("key-1"))).isNull()
    }

    @Test
    fun `만료된 레코드를 일괄 삭제할 수 있다`() {
        val expired = createRecord(
            "expired", RecordStatus.COMPLETED, "응답",
            expiresAt = Instant.parse("2023-12-31T00:00:00Z"),
        )
        val valid = createRecord(
            "valid", RecordStatus.COMPLETED, "응답",
            expiresAt = Instant.parse("2024-12-31T00:00:00Z"),
        )
        store.save(expired)
        store.save(valid)

        store.removeExpired()

        assertThat(store.size()).isEqualTo(1)
        assertThat(store.find(IdempotencyKey("valid"))).isNotNull
    }

    private fun createRecord(
        key: String,
        status: RecordStatus,
        response: String?,
        expiresAt: Instant = Instant.now(clock).plus(Duration.ofHours(24)),
    ) = IdempotencyRecord(
        key = IdempotencyKey(key),
        status = status,
        response = response,
        expiresAt = expiresAt,
    )
}
