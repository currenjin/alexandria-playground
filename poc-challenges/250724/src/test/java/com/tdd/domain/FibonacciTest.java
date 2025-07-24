package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void fibonacci_test() {
		int[][] numbers = {{0,0}, {1,1}, {1,2}};

		for (int[] number : numbers) {
			assertEquals(number[0], Fibonacci.fib(number[1]));
		}
	}
}
