package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FibonacciTest {
	@Test
	void test_fibonacci() {
		int[][] cases = {{0,0}, {1, 1}, {2, 1}, {3, 2}, {4, 3}, {5, 5}, {6, 8}, {7, 13}};

		for (int[] aCase : cases) {
			assertEquals(aCase[1], Fibonacci.fib(aCase[0]));
		}
	}
}
