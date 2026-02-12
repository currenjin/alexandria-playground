package com.currenjin.idempotency.core

interface IdempotencyStore<T> {
    fun find(key: IdempotencyKey): IdempotencyRecord<T>?
    fun save(record: IdempotencyRecord<T>): Boolean
    fun update(record: IdempotencyRecord<T>)
    fun remove(key: IdempotencyKey)
    fun removeExpired()
}
