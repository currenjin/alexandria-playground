package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MinStackTest {
	@Test
	void push_then_top_returnsSameValue() {
		MinStack stack = new MinStack();

		stack.push(5);

		assertEquals(5, stack.top());
	}
}
