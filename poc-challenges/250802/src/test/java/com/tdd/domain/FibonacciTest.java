package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FibonacciTest {
	@ParameterizedTest
	@CsvSource(value = {"0,0", "1,1", "1,2", "2,3", "3,4"})
	void fibonacci_test(int result, int number) {
		assertEquals(result, fib(number));
	}

	private int fib(int n) {
		if (n == 0) return 0;
		if (n <= 2) return 1;
		return fib(n - 1) + 1;
	}
}
