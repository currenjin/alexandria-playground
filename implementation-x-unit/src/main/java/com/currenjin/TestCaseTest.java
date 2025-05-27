package com.currenjin;

public class TestCaseTest extends TestCase {
	public TestCaseTest(String name) {
		super(name);
	}

	public static TestSuite suite() {
        return new TestSuite(TestCaseTest.class);
	}

	public void testTemplateMethod() {
		WasRun wasRun = new WasRun("testMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("setUp testMethod tearDown", wasRun.log);
	}

	public void testResult() {
		WasRun wasRun = new WasRun("testMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("1 run, 0 failed", result.getSummary());
	}

	public void testFailedResultFormatting() {
		TestResult result = new TestResult();
		result.testStarted();
		result.testFailed();
		Assert.assertEquals("1 run, 1 failed", result.getSummary());
	}

	public void testFailedResult() {
		WasRun wasRun = new WasRun("testBrokenMethod");
		TestResult result = new TestResult();
		wasRun.run(result);
		Assert.assertEquals("1 run, 1 failed", result.getSummary());
	}

	public void testSuite() {
		TestSuite suite = new TestSuite();
		suite.add(new WasRun("testMethod"));
		suite.add(new WasRun("testBrokenMethod"));
		TestResult result = new TestResult();
		suite.run(result);
		Assert.assertEquals("2 run, 1 failed", result.getSummary());
	}
}
