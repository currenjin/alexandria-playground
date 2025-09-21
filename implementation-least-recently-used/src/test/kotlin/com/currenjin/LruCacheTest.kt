package com.currenjin

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class LruCacheTest {
    @Test
    fun get_from_empty_returns_null() {
        val cache = LruCache()

        assertNull(cache.get(1))
    }
}