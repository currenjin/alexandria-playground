package com.currenjin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinHeapTest {
    @Test
    fun empty_heap_is_empty() {
        val minHeap = MinHeap()

        assertTrue(minHeap.isEmpty())
    }
}