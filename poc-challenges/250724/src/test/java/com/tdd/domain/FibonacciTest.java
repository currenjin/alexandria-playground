package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void fibonacci_test() {
		int[][] numbers = {{0,0}, {1,1}, {1,2}, {2,3}, {3,4}, {5,5}, {8,6}, {13,7}, {21,8}};

		for (int[] number : numbers) {
			assertEquals(number[0], Fibonacci.fib(number[1]));
		}
	}
}
