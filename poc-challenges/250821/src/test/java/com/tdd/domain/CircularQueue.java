package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CircularQueue {
	private final List<Integer> data = new ArrayList<>();
	private final int capacity;

	public CircularQueue(int i) {
		capacity = i;
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void enqueue(int i) {
		if (isFull()) {
			throw new IllegalStateException("queue is full");
		}

		data.add(i);
	}

	public int peek() {
		validateEmptyQueue();

		return data.get(0);
	}

	public boolean isFull() {
		return data.size() == capacity;
	}

	public int dequeue() {
		validateEmptyQueue();

		return data.remove(0);
	}

	private void validateEmptyQueue() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}
	}
}
