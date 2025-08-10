package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

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
	void push_multipleValues_then_top_returnsLastPushedValue() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.push(2);

		assertEquals(2, stack.top());
	}
}
