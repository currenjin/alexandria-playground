package com.currenjin.performance;

import org.junit.jupiter.api.Test;

class MethodsTimerTest {
	@Test
	void report() throws Exception {
		MethodsTimer tester = new MethodsTimer(ListSearch.class.getDeclaredMethods());

		tester.report();
	}
}