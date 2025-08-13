package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FIFOQueueTest {
	@Test
	void isEmpty_returnsTrue() {
		FIFOQueue stack = new FIFOQueue();

		assertTrue(stack.isEmpty());
	}
}
