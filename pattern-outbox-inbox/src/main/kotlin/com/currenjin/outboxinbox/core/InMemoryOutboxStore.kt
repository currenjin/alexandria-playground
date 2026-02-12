package com.currenjin.outboxinbox.core

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class InMemoryOutboxStore : OutboxStore {
    private val records = ConcurrentHashMap<String, OutboxRecord>()

    override fun save(record: OutboxRecord) {
        records[record.id] = record
    }

    override fun findPending(): List<OutboxRecord> =
        records.values
            .filter { it.status == OutboxStatus.PENDING }
            .sortedBy { it.createdAt }

    override fun markSent(id: String, sentAt: Instant) {
        records.computeIfPresent(id) { _, record ->
            record.copy(status = OutboxStatus.SENT, sentAt = sentAt)
        }
    }

    override fun markFailed(id: String) {
        records.computeIfPresent(id) { _, record ->
            record.copy(status = OutboxStatus.FAILED, retryCount = record.retryCount + 1)
        }
    }

    override fun findAll(): List<OutboxRecord> = records.values.toList()
}
