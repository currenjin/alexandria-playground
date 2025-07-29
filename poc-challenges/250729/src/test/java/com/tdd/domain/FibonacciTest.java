package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FibonacciTest {
	// 0, 1, 1, 2, 3, 5, 8, 13, 21
	@ParameterizedTest
	@CsvSource(value = {"0,0", "1,1", "1,2", "2,3", "3,4", "5,5"})
	void fibonacci_name(int result, int number) {
		assertEquals(result, fib(number));
	}

	private int fib(int i) {
		if (i < 2) return i;
		return fib(i - 1) + fib(i - 2);
	}
}
