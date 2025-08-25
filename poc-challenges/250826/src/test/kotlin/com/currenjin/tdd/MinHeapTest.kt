package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinHeapTest {
    @Test
    fun isEmpty_returnsTrue() {
        val heap = MinHeap()

        assertTrue(heap.isEmpty())
    }
}
