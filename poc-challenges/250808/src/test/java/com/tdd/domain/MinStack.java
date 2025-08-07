package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {
	private final List<Integer> data = new ArrayList<>();

	public int top() {
		if (data.isEmpty()) {
			throw new NoSuchElementException("minStack is empty");
		}

		return data.get(data.size() - 1);
	}

	public void push(int i) {
		data.add(i);
	}

	public void pop() {
		throw new NoSuchElementException("minStack is empty");
	}
}
