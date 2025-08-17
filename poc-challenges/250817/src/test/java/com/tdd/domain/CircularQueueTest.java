package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

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
}
