package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class MinStack {

	private final List<Integer> stack = new ArrayList<>();

	public int pop() {
		if (stack.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}

		return 1;
	}

	public void push(int i) {
		stack.add(i);
	}

	public int getMin() {
		if (stack.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}

		return stack.get(0);
	}
}
