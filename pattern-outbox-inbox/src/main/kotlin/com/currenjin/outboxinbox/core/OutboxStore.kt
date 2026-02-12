package com.currenjin.outboxinbox.core

import java.time.Instant

interface OutboxStore {
    fun save(record: OutboxRecord)
    fun findPending(): List<OutboxRecord>
    fun markSent(id: String, sentAt: Instant)
    fun markFailed(id: String)
    fun findAll(): List<OutboxRecord>
}
