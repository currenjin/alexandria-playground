package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinHeapTest {
    @Test
    fun isEmpty_returnsTrue() {
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

        assertThrows(NoSuchElementException::class.java, heap::peek)
    }

    @Test
    fun add_then_peek_returnsValue() {
        val heap = MinHeap()

        heap.add(1)

        assertEquals(1, heap.peek())
    }

    @Test
    fun add_multipleValues_then_peek_returnsMinValue() {
        val heap = MinHeap()

        heap.add(2)
        heap.add(1)
        heap.add(3)

        assertEquals(1, heap.peek())
    }

    @Test
    fun poll_throwsException_whenEmpty() {
        val heap = MinHeap()

        assertThrows(NoSuchElementException::class.java, heap::poll)
    }

    @Test
    fun add_then_poll_returnsValue() {
        val heap = MinHeap()

        heap.add(1)

        assertEquals(1, heap.poll())
        assertTrue(heap.isEmpty())
    }

    @Test
    fun add_multipleValues_then_poll_returnsMinValueInOrder() {
        val heap = MinHeap()

        heap.add(3)
        heap.add(1)
        heap.add(2)

        assertEquals(1, heap.poll())
        assertEquals(2, heap.poll())
        assertEquals(3, heap.poll())
        assertTrue(heap.isEmpty())
    }
}
