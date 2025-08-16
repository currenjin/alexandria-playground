package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class MinStack {

	private final List<Integer> stack = new ArrayList<>();
	private final List<Integer> min = new ArrayList<>();

	public void pop() {
		validateEmptyStack();

		stack.remove(stack.size() - 1);
		min.remove(min.size() - 1);
	}

	public void push(int i) {
		stack.add(i);
		int minimumValue = min.isEmpty() ? i : Math.min(i, min.get(min.size() - 1));
		min.add(minimumValue);
	}

	public int getMin() {
		validateEmptyStack();

		return min.get(min.size() - 1);
	}

	private void validateEmptyStack() {
		if (stack.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}
	}

	public int top() {
		validateEmptyStack();

		return stack.get(stack.size() - 1);
	}
}
