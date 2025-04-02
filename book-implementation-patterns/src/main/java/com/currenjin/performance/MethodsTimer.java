package com.currenjin.performance;

import java.lang.reflect.Method;

public class MethodsTimer {
	public static final long ONE_SECOND = 1000000000;
	private static final int MAXIMUM_SIZE = 100000;
	private final Method[] methods;

	public MethodsTimer(Method[] methods) {
		this.methods = methods;
	}

	public void report() throws Exception {
		for (Method method : methods) {
			System.out.print(method.getName() + "\t");
			for (int size = 1; size <= MAXIMUM_SIZE; size *= 10) {
				MethodTimer runner = new MethodTimer(size, method);
				runner.run();
				System.out.printf("%.2f\t", runner.getMethodTime());
			}
			System.out.println();
		}
	}

	public static class Overhead {
		public Overhead(int size) {}

		public void nothing() {}
	}
}
