package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class MinHeapTest {
    @Test
    fun isEmpty_returnsTrue_whenEmpty() {
        val heap = MinHeap()

        assertTrue(heap.isEmpty())
    }
}
