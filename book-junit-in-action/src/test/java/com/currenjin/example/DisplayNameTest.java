package com.currenjin.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test class showing the @DisplayName annotation.")
public class DisplayNameTest {
	private SUT sut = new SUT();

	@Test
	@DisplayName("Our system under test says hello.")
	void testHello() {
		assertEquals("Hello", sut.hello());
	}

	@Test
	@DisplayName("🥺")
	void testTalking() {
		assertEquals("How are you?", sut.talk());
	}

	@Test
	void testBye() {
		assertEquals("Bye", sut.bye());
	}

	private class SUT {
		public String talk() {
			return "How are you?";
		}

		public String hello() {
			return "Hello";
		}

		public String bye() {
			return "Bye";
		}
	}
}
