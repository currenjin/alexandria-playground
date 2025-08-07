package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class MinStackTest {
	@Test
	void top_throwsException_whenEmpty() {
		MinStack minStack = new MinStack();

		assertThrows(NoSuchElementException.class, minStack::top);
	}

	@Test
	void push_then_top_returnsPushedValue() {
		MinStack minStack = new MinStack();

		minStack.push(5);

		assertEquals(5, minStack.top());
	}

	@Test
	void push_differentValue_then_top_returnsThatValue() {
		MinStack minStack = new MinStack();

		minStack.push(100);

		assertEquals(100, minStack.top());
	}

	@Test
	void push_multipleValues_then_top_returnsLastPushed() {
		MinStack minStack = new MinStack();

		minStack.push(100);
		minStack.push(1);

		assertEquals(1, minStack.top());
	}

	@Test
	void pop_throwsException_whenStackIsEmpty() {
		MinStack minStack = new MinStack();

		assertThrows(NoSuchElementException.class, minStack::pop);
	}

	@Test
	void push_twoValues_then_pop_then_top_returnsFirst() {
		MinStack minStack = new MinStack();

		minStack.push(100);
		minStack.push(1);

		minStack.pop();

		assertEquals(100, minStack.top());
	}

	@Test
	void getMin_throwsException_whenStackIsEmpty() {
		MinStack minStack = new MinStack();

		assertThrows(NoSuchElementException.class, minStack::getMin);
	}

	@Test
	void push_oneValue_then_getMin_returnsSame() {
		MinStack minStack = new MinStack();

		minStack.push(1);

		assertEquals(1, minStack.getMin());
	}

	@Test
	void push_multipleValues_then_getMin_returnsMinimumValue() {
		MinStack minStack = new MinStack();

		minStack.push(100);
		minStack.push(1);

		assertEquals(1, minStack.getMin());
	}

	@Test
	void push_multipleValues_then_pop_then_getMin_returnsMinimumValue() {
		MinStack minStack = new MinStack();

		minStack.push(100);
		minStack.push(1);
		minStack.push(50);
		minStack.push(40);

		minStack.pop();
		assertEquals(1, minStack.getMin());

		minStack.pop();
		assertEquals(1, minStack.getMin());

		minStack.pop();
		assertEquals(1, minStack.getMin());

		minStack.pop();
		assertThrows(NoSuchElementException.class, minStack::getMin);
	}
}
