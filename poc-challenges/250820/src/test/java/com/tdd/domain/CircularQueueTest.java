package com.tdd.domain;

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
}
