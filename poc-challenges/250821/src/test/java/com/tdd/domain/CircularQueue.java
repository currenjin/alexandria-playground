package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CircularQueue {
	private final List<Integer> data = new ArrayList<>();

	public CircularQueue(int i) {

	}

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

		return data.get(0);
	}
}
