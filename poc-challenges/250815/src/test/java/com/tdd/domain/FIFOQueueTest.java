package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FIFOQueueTest {
	@Test
	void isEmpty_returnsTrue() {
		FIFOQueue queue = new FIFOQueue();

		assertTrue(queue.isEmpty());
	}

	@Test
	void enqueue_then_isEmpty_returnsFalse() {
		FIFOQueue queue = new FIFOQueue();

		queue.enqueue(1);

		assertFalse(queue.isEmpty());
	}
}
