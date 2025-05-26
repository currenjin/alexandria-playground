package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		WasRun wasRun = new WasRun();
		System.out.println(wasRun.wasRun);
		wasRun.testMethod();
		System.out.println(wasRun.wasRun);
	}
}
