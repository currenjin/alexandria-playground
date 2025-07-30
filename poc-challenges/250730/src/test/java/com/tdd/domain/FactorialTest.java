package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FactorialTest {
	@Test
	void factorial_test() {
		assertEquals(1, Factorial.fac(1));
		assertEquals(2, Factorial.fac(2));
	}

	private static class Factorial {
		public static int fac(int i) {
			return i;
		}
	}
}
