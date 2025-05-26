package com.currenjin;

public class TestResult {
	private int runCount = 0;

	public void testStarted() {
		runCount++;
	}

	public String getSummary() {
		return runCount + " run, 0 failed";
	}
}
