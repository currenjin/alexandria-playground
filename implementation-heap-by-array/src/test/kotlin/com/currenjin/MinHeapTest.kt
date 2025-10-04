package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
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

    @Test
    fun add_two_then_one_peek_returns_one() {
        val heap = MinHeap()

        heap.add(1)
        heap.add(2)

        assertEquals(1, heap.peek())
    }

    @Test
    fun add_one_then_poll_returns_it_and_becomes_empty() {
        val heap = MinHeap()
        heap.add(1)

        val polled = heap.poll()

        assertEquals(1, polled)
        assertTrue(heap.isEmpty())
    }

    @Test
    fun after_poll_peek_returns_remaining_element() {
        val heap = MinHeap()

        heap.add(2)
        heap.add(1)

        val first = heap.poll()
        assertEquals(1, first)
        assertEquals(2, heap.peek())
    }

    @Test
    fun poll_three_elements_returns_in_ascending_order() {
        val heap = MinHeap()

        heap.add(3)
        heap.add(1)
        heap.add(2)

        assertEquals(1, heap.poll())
        assertEquals(2, heap.poll())
        assertEquals(3, heap.poll())
        assertTrue(heap.isEmpty())
    }

    @Test
    fun peek_on_empty_throws() {
        val heap = MinHeap()

        assertThrows(NoSuchElementException::class.java) { heap.peek() }
    }
}
