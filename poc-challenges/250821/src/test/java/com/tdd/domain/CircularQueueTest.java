package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;

public class CircularQueueTest {
	@Test
	void isEmpty_returnsTrue() {
		CircularQueue queue = new CircularQueue(1);

		assertTrue(queue.isEmpty());
	}

	@Test
	void enqueue_then_isEmpty_returnsFalse() {
		CircularQueue queue = new CircularQueue(1);

		queue.enqueue(1);

		assertFalse(queue.isEmpty());
	}

	@Test
	void peek_throwsException_whenEmpty() {
		CircularQueue queue = new CircularQueue(1);

		assertThrows(NoSuchElementException.class, queue::peek);
	}

	@Test
	void enqueue_then_peek_returnsValue() {
		CircularQueue queue = new CircularQueue(1);

		queue.enqueue(1);

		assertEquals(1, queue.peek());
	}

	@Test
	void enqueue_multipleValues_then_peek_returnsFirstValue() {
		CircularQueue queue = new CircularQueue(2);

		queue.enqueue(1);
		queue.enqueue(2);

		assertEquals(1, queue.peek());
	}

	@Test
	void isFull_returnsFalse() {
		CircularQueue queue = new CircularQueue(1);

		assertFalse(queue.isFull());
	}

	@Test
	void isFull_returnsTrue_whenFull() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertTrue(queue.isFull());
	}

	@Test
	void enqueue_throwsException_whenFull() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertThrows(IllegalStateException.class, () -> queue.enqueue(4));
	}
}
