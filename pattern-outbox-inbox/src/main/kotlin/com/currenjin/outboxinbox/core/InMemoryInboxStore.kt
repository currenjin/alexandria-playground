package com.currenjin.outboxinbox.core

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class InMemoryInboxStore : InboxStore {
    private val records = ConcurrentHashMap<String, InboxRecord>()

    override fun exists(messageId: String): Boolean = records.containsKey(messageId)

    override fun save(record: InboxRecord) {
        records[record.messageId] = record
    }

    override fun markCompleted(messageId: String) {
        records.computeIfPresent(messageId) { _, record ->
            record.copy(status = InboxStatus.COMPLETED, processedAt = Instant.now())
        }
    }

    override fun markFailed(messageId: String, errorMessage: String) {
        records.computeIfPresent(messageId) { _, record ->
            record.copy(status = InboxStatus.FAILED, errorMessage = errorMessage)
        }
    }

    override fun find(messageId: String): InboxRecord? = records[messageId]

    override fun findAll(): List<InboxRecord> = records.values.toList()
}
