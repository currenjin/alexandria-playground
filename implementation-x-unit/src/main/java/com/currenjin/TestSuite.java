package com.currenjin;

import java.util.*;

public class TestSuite {
	List<WasRun> tests = new ArrayList<>();

	public void add(WasRun test) {
		tests.add(test);
	}

	public void run(TestResult result) {
		tests.forEach(t -> {
			t.run(result);
		});
	}
}
