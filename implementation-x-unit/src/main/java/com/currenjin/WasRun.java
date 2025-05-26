package com.currenjin;

public class WasRun extends TestCase {
	public String log;

	@Override
	public void setUp() {
		log = "setUp";
	}

	@Override
	public void tearDown() {
		log += " tearDown";
	}

	public WasRun(String name) {
		super(name);
	}

	public void testMethod() {
		log += " testMethod";
	}

	public void testBrokenMethod() {
		throw new AssertionError();
	}
}
