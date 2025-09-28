package com.currenjin

import org.junit.jupiter.api.Test
import kotlin.emptyArray
import kotlin.test.assertContentEquals

class InsertionSortTest {
    @Test
    fun empty_array_returns_empty_array() {
        val actual = InsertionSort.sort(emptyArray())

        assertContentEquals(emptyArray(), actual)
    }

    @Test
    fun single_element_array_returns_itself() {
        val actual = InsertionSort.sort(arrayOf(1))

        assertContentEquals(arrayOf(1), actual)
    }

    @Test
    fun two_elements_array_returns_sorted_order() {
        val actual = InsertionSort.sort(arrayOf(2, 1))

        assertContentEquals(arrayOf(1, 2), actual)
    }

    @Test
    fun three_elements_array_sorts_middle_order() {
        val actual = InsertionSort.sort(arrayOf(3, 1, 2))

        assertContentEquals(arrayOf(1, 2, 3), actual)
    }

    @Test
    fun three_elements_array_already_sorted_stays_sorted() {
        val actual = InsertionSort.sort(arrayOf(10, 20, 30))

        assertContentEquals(arrayOf(10, 20, 30), actual)
    }

    @Test
    fun three_elements_array_reverse_sorted_becomes_ascending() {
        val actual = InsertionSort.sort(arrayOf(30, 20, 10))

        assertContentEquals(arrayOf(10, 20, 30), actual)
    }
}