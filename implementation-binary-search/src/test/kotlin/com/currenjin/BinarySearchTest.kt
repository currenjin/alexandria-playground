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

    @Test
    fun two_elements_array_returns_index_1_when_right_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20), 20)

        assertEquals(1, actual)
    }

    @Test
    fun three_elements_array_returns_index_1_when_middle_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30), 20)

        assertEquals(1, actual)
    }

    @Test
    fun three_elements_array_returns_index_2_when_right_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30), 30)

        assertEquals(2, actual)
    }

    @Test
    fun three_elements_array_returns_index_0_when_left_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30), 10)

        assertEquals(0, actual)
    }

    @Test
    fun five_elements_array_returns_index_3_when_element_found() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30, 40, 50), 40)

        assertEquals(3, actual)
    }

    @Test
    fun three_elements_array_returns_index_minus1_when_target_not_found() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30), 25)

        assertEquals(-1, actual)
    }

    @Test
    fun returns_minus1_when_target_not_in_array() {
        val actual = BinarySearch.search(arrayOf(10, 20, 30, 40, 50), 7)

        assertEquals(-1, actual)
    }

    @Test
    fun returns_first_index_when_array_has_duplicates() {
        val actual = BinarySearch.search(arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1), 1)

        assertEquals(0, actual)
    }
}