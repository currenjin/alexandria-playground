package com.currenjin;

import com.currenjin.annotation.Test;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	public static TestSuite suite() {
        return new TestSuite(TestCaseTest.class);
	}

	@Test
	public void templateMethod() {
		WasRun wasRun = new WasRun("testMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("setUp testMethod tearDown", wasRun.log);
	}

	@Test
	public void result() {
		WasRun wasRun = new WasRun("testMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("1 run, 0 failed", result.getSummary());
	}

	@Test
	public void failedResultFormatting() {
		TestResult result = new TestResult();
		result.testStarted();
		result.testFailed();
		Assert.assertEquals("1 run, 1 failed", result.getSummary());
	}

	@Test
	public void failedResult() {
		WasRun wasRun = new WasRun("testBrokenMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("1 run, 1 failed", result.getSummary());
	}

	@Test
	public void suiteTest() {
		TestSuite suite = new TestSuite();
		suite.add(new WasRun("testMethod"));
		suite.add(new WasRun("testBrokenMethod"));
		TestResult result = new TestResult();
		suite.run(result);
		Assert.assertEquals("2 run, 1 failed", result.getSummary());
	}
}
