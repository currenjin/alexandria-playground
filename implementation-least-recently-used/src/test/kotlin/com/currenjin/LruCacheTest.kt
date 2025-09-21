package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class LruCacheTest {
    @Test
    fun get_from_empty_returns_null() {
        val cache = LruCache()

        assertNull(cache.get(1))
    }

    @Test
    fun put_then_get_returns_value() {
        val cache = LruCache()

        cache.put(1, "A")

        assertEquals("A", cache.get(1))
    }

    @Test
    fun put_same_key_overwrites_value() {
        val cache = LruCache()

        cache.put(1, "A")
        cache.put(1, "B")

        assertEquals("B", cache.get(1))
    }

    @Test
    fun capacity1_put_two_evicts_first() {
        val cache = LruCache()

        cache.put(1, "A")
        cache.put(2, "B")

        assertNull(cache.get(1))
        assertEquals("B", cache.get(2))
    }
}
