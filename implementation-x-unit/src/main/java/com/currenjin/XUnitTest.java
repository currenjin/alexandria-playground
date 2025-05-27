package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		TestResult result = new TestResult();
		TestSuite suite = TestCaseTest.suite();

		suite.run(result);

		System.out.println(result.getSummary());
	}
}
