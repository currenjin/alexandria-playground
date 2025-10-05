package com.currenjin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

class MinHeapTest {
    @Test
    fun empty_heap_is_empty() {
        val minHeap = MinHeap<Int>()

        assertTrue(minHeap.isEmpty())
    }

    @Test
    fun add_one_then_heap_is_not_empty() {
        val heap = MinHeap<Int>()

        heap.add(1)

        assertFalse(heap.isEmpty())
    }

    @Test
    fun add_one_then_peek_returns_it() {
        val heap = MinHeap<Int>()

        heap.add(1)

        assertEquals(1, heap.peek())
    }

    @Test
    fun add_two_then_one_peek_returns_one() {
        val heap = MinHeap<Int>()

        heap.add(1)
        heap.add(2)

        assertEquals(1, heap.peek())
    }

    @Test
    fun add_one_then_poll_returns_it_and_becomes_empty() {
        val heap = MinHeap<Int>()
        heap.add(1)

        val polled = heap.poll()

        assertEquals(1, polled)
        assertTrue(heap.isEmpty())
    }

    @Test
    fun after_poll_peek_returns_remaining_element() {
        val heap = MinHeap<Int>()

        heap.add(2)
        heap.add(1)

        val first = heap.poll()
        assertEquals(1, first)
        assertEquals(2, heap.peek())
    }

    @Test
    fun poll_three_elements_returns_in_ascending_order() {
        val heap = MinHeap<Int>()

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
        val heap = MinHeap<Int>()

        assertThrows(NoSuchElementException::class.java) { heap.peek() }
    }

    @Test
    fun poll_on_empty_throws() {
        val heap = MinHeap<Int>()

        assertThrows(NoSuchElementException::class.java) { heap.poll() }
    }

    @Test
    fun poll_returns_values_in_ascending_order() {
        val heap = MinHeap<Int>()

        heap.add(3)
        heap.add(1)
        heap.add(2)

        assertEquals(1, heap.poll())
        assertEquals(2, heap.poll())
        assertEquals(3, heap.poll())
        assertTrue(heap.isEmpty())
    }

    @Test
    fun size_reflects_number_of_elements() {
        val heap = MinHeap<Int>()

        assertEquals(0, heap.size())

        heap.add(1)
        heap.add(2)

        assertEquals(2, heap.size())

        heap.poll()

        assertEquals(1, heap.size())
    }

    @Test
    fun heap_handles_many_random_inserts() {
        val heap = MinHeap<Int>()
        val numbers = List(1000) { Random.nextInt(0, 10_000) }

        numbers.forEach { heap.add(it) }

        val result = mutableListOf<Int>()
        while (!heap.isEmpty()) {
            result += heap.poll()
        }

        assertEquals(numbers.sorted(), result)
    }

    @Test
    fun generic_heap_handles_strings() {
        val heap = MinHeap<String>()

        heap.add("pear")
        heap.add("apple")
        heap.add("banana")

        assertEquals("apple", heap.poll())
        assertEquals("banana", heap.poll())
        assertEquals("pear", heap.poll())
    }

    @Test
    fun custom_comparator_creates_max_heap() {
        val maxHeap = MinHeap(comparator = compareByDescending<Int> { it })

        maxHeap.add(1)
        maxHeap.add(3)
        maxHeap.add(2)

        assertEquals(3, maxHeap.poll())
        assertEquals(2, maxHeap.poll())
        assertEquals(1, maxHeap.poll())
    }
}
