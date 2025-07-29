package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FactorialTest {
	// 1, 2, 6, 24, 120, 720, ...
	@Test
	void factorial_test() {
		int[][] numbers = {{1,1}, {2,2}, {6,3}, {24,4}, {120,5}};

		for (int[] number : numbers) {
			assertEquals(number[0], factorial(number[1]));
		}
	}

	private int factorial(int i) {
		if (i == 1) return 1;
		return factorial(i-1) * i;
	}
}
