package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LruCacheTest {
    @Test
    fun get_from_empty_returns_null() {
        val cache = LruCache<Int, String>()

        assertNull(cache.get(1))
    }

    @Test
    fun put_then_get_returns_value() {
        val cache = LruCache<Int, String>()

        cache.put(1, "A")

        assertEquals("A", cache.get(1))
    }

    @Test
    fun put_same_key_overwrites_value() {
        val cache = LruCache<Int, String>()

        cache.put(1, "A")
        cache.put(1, "B")

        assertEquals("B", cache.get(1))
    }

    @Test
    fun capacity1_put_two_evicts_first() {
        val cache = LruCache<Int, String>()

        cache.put(1, "A")
        cache.put(2, "B")

        assertNull(cache.get(1))
        assertEquals("B", cache.get(2))
    }

    @Test
    fun capacity2_put_two_both_exist() {
        val cache = LruCache<Int, String>(capacity = 2)

        cache.put(1, "A")
        cache.put(2, "B")

        assertEquals("A", cache.get(1))
        assertEquals("B", cache.get(2))
    }

    @Test
    fun get_updates_recency_evicts_least_recent() {
        val cache = LruCache<Int, String>(capacity = 2)

        cache.put(1, "A")
        cache.put(2, "B")

        cache.get(1)

        cache.put(3, "C")

        assertNull(cache.get(2))
        assertEquals("A", cache.get(1))
        assertEquals("C", cache.get(3))
    }

    @Test
    fun generic_key_value_types_work() {
        val cache = LruCache<Long, String>(2)

        cache.put(100L, "foo")

        assertEquals("foo", cache.get(100L))
    }

    @Test
    fun zero_capacity_throws_exception() {
        assertThrows<IllegalArgumentException> { LruCache<Int, String>(capacity = 0) }
    }

    @Test
    fun negative_capacity_throws_exception() {
        assertThrows<IllegalArgumentException> { LruCache<Int, String>(capacity = -1) }
    }

    @Test
    fun size_reflects_number_of_entries() {
        val cache = LruCache<Int, String>(capacity = 2)
        assertEquals(0, cache.size())

        cache.put(1, "A")
        assertEquals(1, cache.size())

        cache.put(2, "B")
        assertEquals(2, cache.size())
    }

    @Test
    fun clear_removes_all_entries() {
        val cache = LruCache<Int, String>(capacity = 2)

        cache.put(1, "A")
        cache.put(2, "B")
        cache.clear()

        assertEquals(0, cache.size())
    }

    @Test
    fun remove_existing_key_deletes_it() {
        val cache = LruCache<Int, String>(capacity = 2)

        cache.put(1, "A")
        cache.put(2, "B")

        cache.remove(1)

        assertNull(cache.get(1))
        assertEquals(1, cache.size())
    }

    @Test
    fun remove_non_existing_key_does_nothing() {
        val cache = LruCache<Int, String>(capacity = 2)

        cache.put(1, "A")

        cache.remove(2)

        assertEquals(1, cache.size())
        assertEquals("A", cache.get(1))
    }
}
