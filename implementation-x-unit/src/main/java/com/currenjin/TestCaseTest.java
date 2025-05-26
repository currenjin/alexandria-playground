package com.currenjin;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	public void testRun() {
		WasRun wasRun = new WasRun("testMethod");
		System.out.println(wasRun.wasRun);
		wasRun.run();
		System.out.println(wasRun.wasRun);
	}
}
