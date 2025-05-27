package com.currenjin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSuite implements Test {
	List<Test> tests = new ArrayList<>();

	public TestSuite(Class<? extends TestCase> testClass) {
		Arrays.stream(testClass.getMethods())
				.filter(method -> method.isAnnotationPresent(com.currenjin.annotation.Test.class))
				.forEach(method -> {
                    try {
						add(testClass.getConstructor(String.class).newInstance(method.getName()));
                    } catch (Exception e) {
						throw new RuntimeException(e);
                    }
                });
	}

	public TestSuite() {

	}

	public void add(Test test) {
		tests.add(test);
	}

	public void run(TestResult result) {
		tests.forEach(t -> t.run(result));
	}
}
