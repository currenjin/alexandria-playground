package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		TestResult result = new TestResult();
		TestSuite suite = TestCaseTest.suite();

		suite.run(result);

		System.out.println(result.getSummary());

		TestSuite suite2 = new TestSuite();
		suite2.add(new TestCaseTest("testTemplateMethod"));
		suite2.add(suite);
		TestResult result2 = new TestResult();
		suite2.run(result2);

		System.out.println(result2.getSummary());
	}
}
