package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class MinStackTest {
	@Test
	void pop_throwsException_WhenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::pop);
	}
}
