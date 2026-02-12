package com.currenjin.idempotency.core

import java.time.Clock
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class InMemoryIdempotencyStore<T>(
    private val clock: Clock = Clock.systemUTC(),
) : IdempotencyStore<T> {
    private val records = ConcurrentHashMap<String, IdempotencyRecord<T>>()

    override fun find(key: IdempotencyKey): IdempotencyRecord<T>? {
        val record = records[key.value] ?: return null
        if (record.expiresAt.isBefore(Instant.now(clock))) {
            records.remove(key.value)
            return null
        }
        return record
    }

    override fun save(record: IdempotencyRecord<T>): Boolean {
        val previous = records.putIfAbsent(record.key.value, record)
        return previous == null
    }

    override fun update(record: IdempotencyRecord<T>) {
        records[record.key.value] = record
    }

    override fun remove(key: IdempotencyKey) {
        records.remove(key.value)
    }

    override fun removeExpired() {
        val now = Instant.now(clock)
        records.entries.removeIf { it.value.expiresAt.isBefore(now) }
    }

    fun size(): Int = records.size
}
