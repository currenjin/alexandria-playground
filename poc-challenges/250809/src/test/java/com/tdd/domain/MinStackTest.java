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

	@Test
	void push_twoValues_then_pop_then_top_returnsFirstValue() {
		MinStack stack = new MinStack();
		stack.push(1);
		stack.push(2);

		stack.pop();

		assertEquals(1, stack.top());
	}

	@Test
	void getMin_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::getMin);
	}

	@Test
	void push_twoValues_then_getMin_returnsMinValue() {
		MinStack stack = new MinStack();

		stack.push(1);
		stack.push(2);

		assertEquals(1, stack.getMin());
	}

	@Test
	void push_multipleValues_then_pop_then_getMin_returnsMinValue() {
		MinStack stack = new MinStack();

		stack.push(3);
		stack.push(100);
		assertEquals(3, stack.getMin());

		stack.pop();
		assertEquals(3, stack.getMin());

		stack.push(1);
		assertEquals(1, stack.getMin());

		stack.pop();
		assertEquals(3, stack.getMin());
	}
}
