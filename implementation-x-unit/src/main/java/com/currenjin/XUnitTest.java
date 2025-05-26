package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		TestResult result = new TestResult();
		TestSuite suite = new TestSuite();

		suite.add(new TestCaseTest("testTemplateMethod"));
		suite.add(new TestCaseTest("testResult"));
		suite.add(new TestCaseTest("testFailedResultFormatting"));
		suite.add(new TestCaseTest("testFailedResult"));
		suite.add(new TestCaseTest("testSuite"));

		suite.run(result);

		System.out.println(result.getSummary());
	}
}
