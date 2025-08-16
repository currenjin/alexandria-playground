package com.currenjin.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreAnnotationsTest {
	private static ResourceForAllTests resourceForAllTests;
	private SUT sut;

	@BeforeAll
	static void beforeAll() {
		resourceForAllTests = new ResourceForAllTests("테스트를 위한 리소스");
	}

	@AfterAll
	static void afterAll() {
		resourceForAllTests.close();
	}

	@BeforeEach
	void setUp() {
		sut = new SUT("테스트 대상 시스템");
	}

	@AfterEach
	void tearDown() {
		sut.close();
	}

	@Test
	void testRegularWork() {
		boolean canReceiveRegularWork = sut.canReceiveRegularWork();

		assertTrue(canReceiveRegularWork);
	}

	@Test
	void testAdditionalWork() {
		boolean canReceiveAdditionalWork = sut.canReceiveAdditionalWork();

		assertFalse(canReceiveAdditionalWork);
	}

	private class SUT {
		public SUT(String system) {

		}

		public boolean canReceiveRegularWork() {
			return true;
		}

		public void close() {

		}

		public boolean canReceiveAdditionalWork() {
			return false;
		}
	}

	private static class ResourceForAllTests {
		public ResourceForAllTests(String resource) {

		}

		public void close() {

		}
	}
}
