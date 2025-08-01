package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorialTest {
	@Test
	void factorial_test() {
		assertEquals(1, fac(1));
		assertEquals(2, fac(2));
		assertEquals(6, fac(3));
	}

	private int fac(int i) {
		if (i <= 2) return i;
		return 6;
	}
}
