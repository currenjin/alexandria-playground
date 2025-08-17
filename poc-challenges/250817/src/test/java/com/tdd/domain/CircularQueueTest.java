package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;

import jdk.internal.org.jline.utils.NonBlockingInputStream;

public class CircularQueueTest {
	@Test
	void isEmpty_returnsTrue_whenEmpty() {
		CircularQueue queue = new CircularQueue(3);

		assertTrue(queue.isEmpty());
	}

	@Test
	void enqueue_thenIsEmpty_returnsFalse() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);

		assertFalse(queue.isEmpty());
	}

	@Test
	void peek_throwsException_whenEmpty() {
		CircularQueue queue = new CircularQueue(3);

		assertThrows(NoSuchElementException.class, queue::peek);
	}

	@Test
	void enqueue_thenPeek_returnsValue() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);

		assertEquals(1, queue.peek());
	}

	@Test
	void dequeue_throwsException_whenEmpty() {
		CircularQueue queue = new CircularQueue(3);

		assertThrows(NoSuchElementException.class, queue::dequeue);
	}

	@Test
	void enqueue_thenDequeue_returnsValue() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);

		assertEquals(1, queue.dequeue());
	}

	@Test
	void enqueueMultipleValues_thenDequeue_returnsValuesInOrder() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);
		queue.enqueue(2);

		assertEquals(1, queue.dequeue());
		assertEquals(2, queue.dequeue());
	}

	@Test
	void enqueue_thenIsFull_returnsTrue_whenFull() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertTrue(queue.isFull());
	}

	@Test
	void enqueue_throwsException_whenFull() {
		CircularQueue queue = new CircularQueue(1);

		queue.enqueue(1);

		assertThrows(IllegalStateException.class, () -> queue.enqueue(3));
	}

	@Test
	void wrap_around() {
		CircularQueue q = new CircularQueue(3);

		q.enqueue(1);
		q.enqueue(2);
		q.enqueue(3);
		assertEquals(1, q.dequeue());

		q.enqueue(4);
		assertEquals(2, q.dequeue());
		assertEquals(3, q.dequeue());
		assertEquals(4, q.dequeue());

		assertTrue(q.isEmpty());
	}

	@Test
	void interleaved_operations_consistent() {
		CircularQueue q = new CircularQueue(3);
		q.enqueue(10);
		assertEquals(10, q.peek());

		q.enqueue(20);
		assertEquals(10, q.dequeue());

		q.enqueue(30);
		q.enqueue(40);
		assertTrue(q.isFull());
		assertEquals(20, q.dequeue());
		assertEquals(30, q.dequeue());
		assertEquals(40, q.dequeue());
		assertTrue(q.isEmpty());
	}

}
