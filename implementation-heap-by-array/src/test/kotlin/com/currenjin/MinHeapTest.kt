package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinHeapTest {
    @Test
    fun empty_heap_is_empty() {
        val minHeap = MinHeap()

        assertTrue(minHeap.isEmpty())
    }

    @Test
    fun add_one_then_heap_is_not_empty() {
        val heap = MinHeap()

        heap.add(1)

        assertFalse(heap.isEmpty())
    }

    @Test
    fun add_one_then_peek_returns_it() {
        val heap = MinHeap()

        heap.add(1)

        assertEquals(1, heap.peek())
    }
}
