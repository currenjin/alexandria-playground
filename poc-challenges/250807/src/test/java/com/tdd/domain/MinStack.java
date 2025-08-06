package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;

public class MinStack {
	private List<Integer> data = new ArrayList<>();

	public void push(int n) {
		data.add(n);
	}

	public int top() {
		return data.get(data.size() - 1);
	}
}
