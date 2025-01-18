package com.currenjin.concurrency;

public class StringStack {
	private String[] values = new String[16];
	private int current = 0;

	public boolean push(String s) {
		// 예외 생략
		if (current < values.length) {
			values[current++] = s;
			current = current + 1;
			return true;
		}
		return false;
	}

	public String pop() {
		if (current < 1) {
			return null;
		}
		current = current - 1;
		return values[current];
	}
}
