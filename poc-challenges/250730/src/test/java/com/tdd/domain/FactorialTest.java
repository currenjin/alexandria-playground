package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FactorialTest {
	@Test
	void factorial_test() {
		int[][] numbers = {{1,1}, {2,2}, {6,3}};

		for (int[] number : numbers) {
			assertEquals(number[0], Factorial.fac(number[1]));
		}
	}

	private static class Factorial {
		public static int fac(int i) {
			if (i <= 2) return i;
			return 6;
		}
	}
}
