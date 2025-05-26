package com.currenjin;

import java.lang.reflect.*;

public class WasRun {
	private final String name;
	public boolean wasRun;

	public WasRun(String name) {
		this.name = name;
	}

	public void testMethod() {
		wasRun = true;
	}

	public void run() {
		try {
			Method method = getClass().getMethod(name);
			method.invoke(this);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
