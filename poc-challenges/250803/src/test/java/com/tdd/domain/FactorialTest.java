package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FactorialTest {
	@Test
	void factorial_test() {
		assertEquals(1, fac(1));
	}

	private int fac(int n) {
		return 0;
	}
}
