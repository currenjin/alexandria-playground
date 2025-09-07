package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

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
}