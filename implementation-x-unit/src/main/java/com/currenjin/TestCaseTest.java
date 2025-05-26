package com.currenjin;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	public void testTemplateMethod() {
		WasRun wasRun = new WasRun("testMethod");
		wasRun.run();
		Assert.assertEquals("setUp testMethod", wasRun.log);
	}
}
