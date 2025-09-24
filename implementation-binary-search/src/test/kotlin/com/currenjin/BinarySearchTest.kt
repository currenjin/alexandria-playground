package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BinarySearchTest {
    @Test
    fun single_element_array_returns_index_0_when_found() {
        val actual = BinarySearch.search(arrayOf(42), 42)

        assertEquals(0, actual)
    }
}