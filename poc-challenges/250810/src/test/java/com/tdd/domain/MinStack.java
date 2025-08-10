package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {

	public static final String STACK_IS_EMPTY = "stack is empty";
	private final List<Integer> data = new ArrayList<>();

	public int top() {
		if (data.isEmpty()) {
			throw new NoSuchElementException(STACK_IS_EMPTY);
		}

		return data.get(data.size() - 1);
	}

	public void push(int i) {
		data.add(i);
	}

	public void pop() {
		throw new NoSuchElementException(STACK_IS_EMPTY);
	}
}
