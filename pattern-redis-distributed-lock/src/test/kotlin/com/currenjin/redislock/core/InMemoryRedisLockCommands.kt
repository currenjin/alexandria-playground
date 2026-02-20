package com.currenjin.redislock.core

import com.currenjin.redislock.redis.RedisLockCommands
import kotlin.time.Duration

class InMemoryRedisLockCommands : RedisLockCommands {
    private data class Entry(
        val value: String,
        var expireAtMillis: Long,
    )

    private val storage = mutableMapOf<String, Entry>()
    private var nowMillis = 0L

    override fun setIfAbsent(key: String, value: String, ttl: Duration): Boolean {
        evictExpiredKey(key)

        if (storage.containsKey(key)) {
            return false
        }

        storage[key] = Entry(value = value, expireAtMillis = nowMillis + ttl.inWholeMilliseconds)
        return true
    }

    override fun compareAndDelete(key: String, expectedValue: String): Boolean {
        evictExpiredKey(key)

        val entry = storage[key] ?: return false
        if (entry.value != expectedValue) {
            return false
        }

        storage.remove(key)
        return true
    }

    override fun compareAndExpire(key: String, expectedValue: String, ttl: Duration): Boolean {
        evictExpiredKey(key)

        val entry = storage[key] ?: return false
        if (entry.value != expectedValue) {
            return false
        }

        entry.expireAtMillis = nowMillis + ttl.inWholeMilliseconds
        return true
    }

    fun advanceTimeBy(duration: Duration) {
        nowMillis += duration.inWholeMilliseconds
    }

    private fun evictExpiredKey(key: String) {
        val entry = storage[key] ?: return
        if (entry.expireAtMillis <= nowMillis) {
            storage.remove(key)
        }
    }
}
