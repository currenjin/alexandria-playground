package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HashMapTest {
    @Test
    fun new_map_has_zero_size() {
        val map = MyHashMap<String, Int?>()

        assertEquals(0, map.size)
    }

    @Test
    fun new_map_is_empty() {
        val map = MyHashMap<String, Int?>()

        assertTrue(map.isEmpty())
    }

    @Test
    fun new_map_containsKey_returns_false() {
        val map = MyHashMap<String, Int?>()

        assertFalse(map.containsKey("a"))
    }

    @Test
    fun new_map_get_returns_null() {
        val map = MyHashMap<String, Int?>()

        assertNull(map.get("a"))
    }

    @Test
    fun put_once_makes_size_1() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)

        assertEquals(1, map.size)
    }

    @Test
    fun put_once_makes_isEmpty_false() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)

        assertFalse(map.isEmpty())
    }

    @Test
    fun put_sets_containsKey_true_for_that_key() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)

        assertTrue(map.containsKey("a"))
    }

    @Test
    fun put_then_get_returns_value() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)

        assertEquals(1, map.get("a"))
    }

    @Test
    fun put_two_distinct_keys_makes_size_2() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)
        map.put("b", 2)

        assertEquals(2, map.size)
    }

    @Test
    fun containsKey_is_true_for_each_inserted_key() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)
        map.put("b", 2)

        assertTrue(map.containsKey("a"))
        assertTrue(map.containsKey("b"))
    }

    @Test
    fun get_returns_value_for_each_inserted_key() {
        val map = MyHashMap<String, Int?>()

        map.put("a", 1)
        map.put("b", 2)

        assertEquals(1, map.get("a"))
        assertEquals(2, map.get("b"))
    }

    @Test
    fun handles_collision_by_chaining() {
        val map = MyHashMap<BadHashKey, Int?>()
        val k1 = BadHashKey("a")
        val k2 = BadHashKey("b")

        map.put(k1, 1)
        map.put(k2, 2)

        assertEquals(1, map.get(k1))
        assertEquals(2, map.get(k2))
    }
}

private data class BadHashKey(val key: String) {
    override fun equals(other: Any?): Boolean = other is BadHashKey && other.key == key

    override fun hashCode(): Int = 42
}
