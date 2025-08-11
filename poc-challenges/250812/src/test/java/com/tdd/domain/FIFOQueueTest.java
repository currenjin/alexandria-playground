package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

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

	@Test
	void enqueue_multipleValues_then_peek_returnsValue() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);
		stack.enqueue(2);
		stack.enqueue(3);

		assertEquals(1, stack.peek());
	}

	@Test
	void dequeue_throwsException_whenEmpty() {
		FIFOQueue stack = new FIFOQueue();

		assertThrows(NoSuchElementException.class, stack::dequeue);
	}

	@Test
	void enqueue_then_dequeue_returnsValue() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);

		assertEquals(1, stack.dequeue());
	}

	@Test
	void enqueue_multipleValues_then_dequeue_returnsFirstValue() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(1);
		stack.enqueue(2);
		stack.enqueue(3);

		assertEquals(1, stack.dequeue());
		assertEquals(2, stack.dequeue());
		assertEquals(3, stack.dequeue());
		assertTrue(stack.isEmpty());
	}

	@Test
	void dequeue_then_peek_moves_to_next() {
		FIFOQueue stack = new FIFOQueue();

		stack.enqueue(2);
		stack.enqueue(3);

		assertEquals(2, stack.dequeue());
		assertEquals(3, stack.peek());
	}

	@Test
	void interleaved_operations_behave_consistently() {
		FIFOQueue stack = new FIFOQueue();
		stack.enqueue(1);
		stack.enqueue(2);
		assertEquals(1, stack.dequeue());

		stack.enqueue(3);
		assertEquals(2, stack.peek());
		assertEquals(2, stack.dequeue());
		assertEquals(3, stack.dequeue());
		assertTrue(stack.isEmpty());
	}
}
