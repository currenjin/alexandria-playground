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

    @Test
    fun update_changes_the_sum() {
        val tree = SegmentTree(arrayOf(5, 7, 3))

        tree.update(1, 10)
        val actual = tree.query(0, 2)

        assertEquals(18, actual)
    }

    @Test
    fun multiple_elements_array_queries() {
        val tree = SegmentTree(arrayOf(10, 20, 30, 40, 50))

        assertEquals(30, tree.query(0, 1))
        assertEquals(90, tree.query(1, 3))
        assertEquals(150, tree.query(0, 4))
    }
}