package com.currenjin.rediscache

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.Duration

class RedisStyleCacheTest {

    @Test
    fun put_then_get_returns_value() {
        val cache = RedisStyleCache<String>()

        cache.put("user:1", "currenjin", Duration.ofMinutes(5))

        assertEquals("currenjin", cache.get("user:1"))
    }

    @Test
    fun get_returns_null_after_ttl_expiration() {
        var now = 1_000L
        val cache = RedisStyleCache<String>(nowMs = { now })

        cache.put("token", "abc", Duration.ofSeconds(10))
        now += 11_000

        assertNull(cache.get("token"))
    }

    @Test
    fun keys_are_normalized_with_trim_and_lowercase() {
        val cache = RedisStyleCache<String>()

        cache.put("  Search:Tracks:Beatles  ", "cached", Duration.ofMinutes(1))

        assertEquals("cached", cache.get("search:tracks:beatles"))
    }

    @Test
    fun evict_removes_value() {
        val cache = RedisStyleCache<String>()

        cache.put("playlist:42", "data", Duration.ofMinutes(1))
        cache.evict("playlist:42")

        assertNull(cache.get("playlist:42"))
    }
}
