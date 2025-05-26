package com.currenjin;

import java.util.*;

public class TestSuite {
	List<TestCase> tests = new ArrayList<>();

	public void add(TestCase test) {
		tests.add(test);
	}

	public void run(TestResult result) {
		tests.forEach(t -> {
			t.run(result);
		});
	}
}
