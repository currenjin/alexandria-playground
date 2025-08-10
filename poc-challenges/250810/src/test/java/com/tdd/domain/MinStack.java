package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {

	public static final String STACK_IS_EMPTY = "stack is empty";
	private final List<Integer> data = new ArrayList<>();
	private final List<Integer> minData = new ArrayList<>();

	public int top() {
		validateEmptyStack();

		return data.get(data.size() - 1);
	}

	public void push(int i) {
		data.add(i);

		int min = minData.isEmpty() ? i : minData.get(minData.size() - 1);
		minData.add(min);
	}

	public void pop() {
		validateEmptyStack();

		data.remove(data.size() - 1);
	}

	private void validateEmptyStack() {
		if (data.isEmpty()) {
			throw new NoSuchElementException(STACK_IS_EMPTY);
		}
	}

	public int getMin() {
		validateEmptyStack();

		return minData.get(minData.size() - 1);
	}
}
