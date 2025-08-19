package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CircularQueueTest {
	@Test
	void isEmpty_returnsTrue_whenEmpty() {
		CircularQueue queue = new CircularQueue(1);

		assertTrue(queue.isEmpty());
	}
}
