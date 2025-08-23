package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;

public class FIFOQueue {
	private Deque<Integer> data = new ArrayDeque<>();

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void enqueue(int i) {
		data.add(i);
	}
}
