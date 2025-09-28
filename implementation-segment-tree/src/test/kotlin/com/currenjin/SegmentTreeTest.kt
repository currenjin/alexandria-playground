package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SegmentTreeTest {
    @Test
    fun single_element_array_returns_its_value() {
        val tree = SegmentTree(arrayOf(5))

        val actual = tree.query(0, 0)

        assertEquals(5, actual)
    }

    @Test
    fun two_elements_array_returns_sum_of_both() {
        val tree = SegmentTree(arrayOf(5, 7))

        val actual = tree.query(0, 1)

        assertEquals(12, actual)
    }

    @Test
    fun three_elements_array_returns_middle_element() {
        val tree = SegmentTree(arrayOf(5, 7, 3))

        val actual = tree.query(1, 1)

        assertEquals(7, actual)
    }

    @Test
    fun three_elements_array_returns_total_sum() {
        val tree = SegmentTree(arrayOf(5, 7, 3))

        val actual = tree.query(0, 2)

        assertEquals(15, actual)
    }
}