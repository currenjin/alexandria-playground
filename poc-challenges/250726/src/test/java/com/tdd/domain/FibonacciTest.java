package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FibonacciTest {
	@ParameterizedTest
	@CsvSource(value = {"0,0", "1,1", "1,2", "2,3", "3,4", "5,5"})
	void fibonacci(int result, int n) {
		assertEquals(result, Fibonacci.fib(n));
	}
}
