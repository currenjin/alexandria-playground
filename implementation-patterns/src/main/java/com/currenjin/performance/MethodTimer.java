package com.currenjin.performance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MethodTimer {
	private final int size;
	private final Method method;
	private Object instance;

	private long totalTime;
	private int iterations;
	private long overhead;

	public MethodTimer(int size, Method method) throws Exception {
		this.size = size;
		this.method = method;
		this.instance = createInstance();
	}

	public MethodTimer(int iterations) throws Exception {
		this(0, MethodsTimer.Overhead.class.getMethod("nothing"));
		this.iterations = iterations;
	}

	private Object createInstance() throws Exception {
		Constructor<?> constructor = method.getDeclaringClass().getConstructor(int.class);
		return constructor.newInstance(size);
	}

	public void run() throws Exception {
		iterations = 1;
		while (true) {
			totalTime = computeTotalTime();
			if (totalTime > MethodsTimer.ONE_SECOND) break;
			iterations *= 2;
		}
		overhead = overheadTimer(iterations).computeTotalTime();
	}

	private MethodTimer overheadTimer(int iterations) throws Exception {
		return new MethodTimer(iterations);
	}

	private long computeTotalTime() {
		return 0;
	}

	double getMethodTime() {
		return (double) (totalTime - overhead) / (double) iterations;
	}
}
