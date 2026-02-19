package com.currenjin.rediscache

import java.time.Duration

class RedisStyleCache<V>(
    private val nowMs: () -> Long = { System.currentTimeMillis() },
) {
    private val store = mutableMapOf<String, CacheEntry<V>>()

    fun get(rawKey: String): V? {
        val key = normalize(rawKey)
        val entry = store[key] ?: return null

        if (entry.expiresAtMs <= nowMs()) {
            store.remove(key)
            return null
        }

        return entry.value
    }

    fun put(rawKey: String, value: V, ttl: Duration) {
        val key = normalize(rawKey)
        store[key] = CacheEntry(value = value, expiresAtMs = nowMs() + ttl.toMillis())
    }

    fun evict(rawKey: String) {
        store.remove(normalize(rawKey))
    }

    private fun normalize(rawKey: String): String {
        return rawKey.trim().lowercase()
    }

    private data class CacheEntry<T>(
        val value: T,
        val expiresAtMs: Long,
    )
}
