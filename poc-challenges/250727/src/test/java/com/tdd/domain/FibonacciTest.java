package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void fibonacci_test() {
		assertEquals(0, Fibonacci.fib(0));
		assertEquals(1, Fibonacci.fib(1));
		assertEquals(1, Fibonacci.fib(2));
		assertEquals(2, Fibonacci.fib(3));
	}
}
