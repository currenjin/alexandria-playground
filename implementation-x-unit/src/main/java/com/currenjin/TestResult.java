package com.currenjin;

public class TestResult {
	private int runCount = 0;
	private int failedCount = 0;

	public void testStarted() {
		runCount++;
	}

	public void testFailed() {
		failedCount++;
	}

	public String getSummary() {
		return runCount + " run, " + failedCount + " failed";
	}
}
