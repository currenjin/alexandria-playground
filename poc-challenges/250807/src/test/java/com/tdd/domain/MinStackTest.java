package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.DoubleSummaryStatistics;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class MinStackTest {
	@Test
	void push_then_top_returnsSameValue() {
		MinStack stack = new MinStack();

		stack.push(5);

		assertEquals(5, stack.top());
	}

	@Test
	void top_throwsException_whenStackIsEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::top);
	}
}
