package com.currenjin.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CalculatorTest {

	@Test
	void testAdd() {
		Calculator calculator = new Calculator();

		double result = calculator.add(10, 50);

		assertEquals(60, result, 0);
	}
}