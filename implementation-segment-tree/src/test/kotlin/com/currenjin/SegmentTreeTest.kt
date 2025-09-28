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
}