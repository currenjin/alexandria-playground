package com.tdd.domain;

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
	void push_doesNotAffect_top_whenNotImplemented() {
		MinStack minStack = new MinStack();

		minStack.push(5);

		assertThrows(NoSuchElementException.class, minStack::top);
	}
}
