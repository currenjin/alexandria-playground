package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class MinStackTest {
	@Test
	void pop_throwsException_WhenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::pop);
	}

	@Test
	void push_then_pop_returnsValue() {
		MinStack stack = new MinStack();

		stack.push(1);

		assertEquals(1, stack.pop());
	}

	@Test
	void push_multipleValues_then_pop_returnsLastValue() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.push(2);
		stack.push(3);

		assertEquals(1, stack.pop());
	}
}
