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
}
