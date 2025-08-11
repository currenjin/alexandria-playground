package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.*;

import org.junit.jupiter.api.Test;

public class FIFOQueueTest {
	@Test
	void emptyQueue_returnsTrueOnIsEmpty() {
		FIFOQueue stack = new FIFOQueue();

		assertTrue(stack.isEmpty());
	}

	@Test
	void enqueue_then_isEmpty_returnsFalse() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);

		assertFalse(stack.isEmpty());
	}
}
