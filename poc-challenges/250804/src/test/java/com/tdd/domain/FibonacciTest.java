package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void fibonacci() {
		assertEquals(0, fib(0));
		assertEquals(1, fib(1));
	}

	private int fib(int n) {
		return n;
	}
}
