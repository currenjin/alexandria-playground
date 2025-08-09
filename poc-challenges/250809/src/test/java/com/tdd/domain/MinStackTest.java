package com.tdd.domain;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MinStackTest {
	@Test
	void top_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::top);
	}

	@Test
	void push_then_top_returnsPushedValue() {
		MinStack stack = new MinStack();

		stack.push(1);

		assertEquals(1, stack.top());
	}

	@Test
	void push_multipleValues_then_top_returnsLastPushed() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.push(10);

		assertEquals(10, stack.top());
	}

	@Test
	void pop_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::pop);
	}
}
