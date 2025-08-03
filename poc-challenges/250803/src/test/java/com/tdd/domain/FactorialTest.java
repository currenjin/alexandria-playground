package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FactorialTest {
	@ParameterizedTest
	@CsvSource(value = {"1,1", "2,2"})
	void factorial_test(int result, int number) {
		assertEquals(result, fac(number));
	}

	private int fac(int n) {
		return n;
	}
}
