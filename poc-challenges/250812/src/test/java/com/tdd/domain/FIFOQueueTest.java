package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import javax.swing.*;

import org.junit.jupiter.api.Test;

public class FIFOQueueTest {
	@Test
	void emptyQueue_returnsTrue() {
		FIFOQueue stack = new FIFOQueue();

		assertTrue(stack.isEmpty());
	}

	@Test
	void enqueue_then_isEmpty_returnsFalse() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);

		assertFalse(stack.isEmpty());
	}

	@Test
	void peek_throwsException_whenEmpty() {
		FIFOQueue stack = new FIFOQueue();

		assertThrows(NoSuchElementException.class, stack::peek);
	}

	@Test
	void enqueue_then_peek_returnsValue() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);

		assertEquals(1, stack.peek());
	}
}
