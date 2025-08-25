package com.currenjin.tdd

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
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

    @Test
    fun peek_throwsException_whenEmpty() {
        val heap = MinHeap()

        assertThrows<NoSuchElementException> { heap.peek() }
    }

    @Test
    fun add_then_peek_returnsValue() {
        val heap = MinHeap()

        heap.add(1)

        assertEquals(1, heap.peek())
    }
}
