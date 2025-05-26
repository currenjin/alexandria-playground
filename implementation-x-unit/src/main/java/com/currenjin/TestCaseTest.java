package com.currenjin;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	WasRun wasRun;

	@Override
	public void setUp() {
		wasRun = new WasRun("testMethod");
	}

	public void testRun() {
		Assert.assertEquals(false, wasRun.wasRun);
		wasRun.run();
		Assert.assertEquals(true, wasRun.wasRun);
	}

	public void testSetUp() {
		Assert.assertEquals(false, wasRun.wasSetUp);
		wasRun.run();
		Assert.assertEquals(true, wasRun.wasSetUp);
	}
}
