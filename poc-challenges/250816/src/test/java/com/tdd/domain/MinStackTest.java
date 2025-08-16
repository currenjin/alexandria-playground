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
	void push_then_pop_then_top_throwsException() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.pop();

		assertThrows(NoSuchElementException.class, stack::top);
	}

	@Test
	void push_multipleValues_then_pop_returnsLastValue() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.push(2);
		stack.push(3);
		assertEquals(3, stack.top());

		stack.pop();

		assertEquals(2, stack.top());
	}

	@Test
	void getMin_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::getMin);
	}

	@Test
	void push_then_getMin_returnsValue() {
		MinStack stack = new MinStack();

		stack.push(1);

		assertEquals(1, stack.getMin());
	}

	@Test
	void push_multipleValues_then_getMin_returnsMinValue() {
		MinStack stack = new MinStack();

		stack.push(2);
		stack.push(3);
		stack.push(1);

		assertEquals(1, stack.getMin());
	}

	@Test
	void top_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::top);
	}

	@Test
	void push_then_top_returnsValue() {
		MinStack stack = new MinStack();

		stack.push(1);

		assertEquals(1, stack.top());
	}

	@Test
	void push_multipleValues_then_top_returnsLastValue() {
		MinStack stack = new MinStack();

		stack.push(1);

		assertEquals(1, stack.top());
	}
}
