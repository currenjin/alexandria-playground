package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void fibonacci() {
		assertEquals(0, Fibonacci.fib(0));
		assertEquals(1, Fibonacci.fib(1));
		assertEquals(1, Fibonacci.fib(2));
	}
}
