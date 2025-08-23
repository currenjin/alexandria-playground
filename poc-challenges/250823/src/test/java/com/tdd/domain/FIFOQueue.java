package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {
	private Deque<Integer> data = new ArrayDeque<>();

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void enqueue(int i) {
		data.add(i);
	}

	public int peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}

		return data.peek();
	}
}
