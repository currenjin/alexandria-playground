package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FIFOQueueTest {
	@Test
	void emptyQueue_returnsTrueOnIsEmpty() {
		FIFOQueue stack = new FIFOQueue();

		assertTrue(stack.isEmpty());
	}
}
