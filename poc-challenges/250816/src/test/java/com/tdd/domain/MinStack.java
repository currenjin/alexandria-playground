package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class MinStack {

	private final List<Integer> stack = new ArrayList<>();

	public int pop() {
		validateEmptyStack();

		return 1;
	}

	public void push(int i) {
		stack.add(i);
	}

	public int getMin() {
		validateEmptyStack();

		return stack.get(0);
	}

	private void validateEmptyStack() {
		if (stack.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}
	}
}
