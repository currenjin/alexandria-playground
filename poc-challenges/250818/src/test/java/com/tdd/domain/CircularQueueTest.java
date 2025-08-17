package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.management.openmbean.TabularDataSupport;

import org.junit.jupiter.api.Test;

public class CircularQueueTest {
	@Test
	void isEmpty_returnsTrue_whenEmpty() {
		CircularQueue queue = new CircularQueue(3);

		assertTrue(queue.isEmpty());
	}

	@Test
	void enqueue_then_isEmpty_returnsFalse() {
		CircularQueue queue = new CircularQueue(3);

		queue.enqueue(1);

		assertFalse(queue.isEmpty());
	}
}
