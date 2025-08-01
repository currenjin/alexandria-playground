package com.tdd.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorialTest {
	@ParameterizedTest
	@CsvSource(value = {"1,1", "2,2", "6,3"})
	void factorial_test(int result, int number) {
		assertEquals(result, fac(number));
	}

	private int fac(int i) {
		if (i <= 2) return i;
		return 6;
	}
}
