package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {
	private List<Integer> data = new ArrayList<>();
	private List<Integer> minStack = new ArrayList<>();

	public void push(int n) {
		data.add(n);
		int min = minStack.isEmpty()
			? n
			: Math.min(n, minStack.get(minStack.size() - 1));
		minStack.add(min);
	}

	public int top() {
		if (data.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}

		return data.get(data.size() - 1);
	}

	public int getMin() {
		if (minStack.isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}
		return minStack.get(minStack.size() - 1);
	}
}
