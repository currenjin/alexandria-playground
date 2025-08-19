package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class CircularQueueTest {
	@Test
	void isEmpty_returnsTrue_whenEmpty() {
		CircularQueue queue = new CircularQueue(1);

		assertTrue(queue.isEmpty());
	}

	@Test
	void enqueue_thenIsEmpty_returnsFalse() {
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
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertEquals(1, queue.peek());
	}
}
