package com.tdd.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DriverTest {
	@Test
	void create() {
		Driver actual = new Driver("name", "010-1234-1234");

		assertEquals(actual.getName(), "name");
		assertEquals(actual.getPhoneNumber(), "010-1234-1234");
	}
}