package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HashMapTest {
    @Test
    fun new_map_has_zero_size() {
        val map = MyHashMap<String, Int?>()

        assertEquals(0, map.size)
    }
}