package com.tdd.domain;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MinStackTest {
	@Test
	void top_throwsException_whenEmpty() {
		MinStack stack = new MinStack();

		assertThrows(NoSuchElementException.class, stack::top);
	}
}
