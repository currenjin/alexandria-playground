package com.currenjin;

public class XUnitTest {
	public static void main(String[] args) {
		WasRun wasRun = new WasRun("testMethod");
		System.out.println(wasRun.wasRun);
		wasRun.run();
		System.out.println(wasRun.wasRun);
	}
}
