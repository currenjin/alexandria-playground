package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MinHeapTest {
    @Test
    fun isEmpty_returnsTrue_whenEmpty() {
        val heap = MinHeap()

        assertTrue(heap.isEmpty())
    }

    @Test
    fun add_then_isEmpty_returnsFalse() {
        val heap = MinHeap()

        heap.add(1)

        assertFalse(heap.isEmpty())
    }
}
