package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BinarySearchTest {
    @Test
    fun single_element_array_returns_index_0_when_found() {
        val actual = BinarySearch.search(arrayOf(42), 42)

        assertEquals(0, actual)
    }

    @Test
    fun two_elements_array_returns_index_0_when_left_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20), 10)

        assertEquals(0, actual)
    }
}