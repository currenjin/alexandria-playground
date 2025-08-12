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

	@Test
	void enqueue_then_peek_returnsValue() {
		FIFOQueue queue = new FIFOQueue();

		queue.enqueue(1);

		assertEquals(1, queue.peek());
	}

	@Test
	void peek_throwsException_whenEmpty() {
		FIFOQueue queue = new FIFOQueue();

		assertThrows(NoSuchElementException.class, queue::peek);
	}

	@Test
	void enqueue_multipleValues_then_peek_returnsFirstValue() {
		FIFOQueue queue = new FIFOQueue();

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertEquals(1, queue.peek());
	}

	@Test
	void dequeue_throwsException_whenEmpty() {
		FIFOQueue queue = new FIFOQueue();

		assertThrows(NoSuchElementException.class, queue::dequeue);
	}

	@Test
	void enqueue_then_dequeue_returnsValue() {
		FIFOQueue queue = new FIFOQueue();

		queue.enqueue(1);

		assertEquals(1, queue.dequeue());
	}

	@Test
	void enqueue_multipleValues_then_dequeue_returnsFirstValue() {
		FIFOQueue queue = new FIFOQueue();

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		assertEquals(1, queue.dequeue());
	}
}
