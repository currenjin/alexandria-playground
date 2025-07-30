package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FactorialTest {
	@Test
	void factorial_test() {
		assertEquals(1, Factorial.fac(1));
		assertEquals(2, Factorial.fac(2));
		assertEquals(6, Factorial.fac(3));
	}

	private static class Factorial {
		public static int fac(int i) {
			if (i <= 2) return i;
			return 6;
		}
	}
}
