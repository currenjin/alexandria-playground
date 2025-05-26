package com.currenjin;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	public void testTemplateMethod() {
		WasRun wasRun = new WasRun("testMethod");
		wasRun.run();
		Assert.assertEquals("setUp testMethod tearDown", wasRun.log);
	}

	public void testResult() {
		WasRun wasRun = new WasRun("testMethod");
		TestResult result = wasRun.run();
		Assert.assertEquals("1 run, 0 failed", result.getSummary());
	}
}
