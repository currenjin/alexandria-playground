package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		TestResult result = new TestResult();
		new TestCaseTest("testTemplateMethod").run(result);
		new TestCaseTest("testResult").run(result);
		new TestCaseTest("testFailedResultFormatting").run(result);
		new TestCaseTest("testFailedResult").run(result);
		new TestCaseTest("testSuite").run(result);
		System.out.println(result.getSummary());
	}
}
