package com.currenjin;

import java.lang.reflect.*;

public class TestCase {
	protected final String name;

	public TestCase(String name) {
		this.name = name;
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
