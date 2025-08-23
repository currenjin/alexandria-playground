package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {
	private final Deque<Integer> data = new ArrayDeque<>();

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public void enqueue(int i) {
		data.add(i);
	}

	public int peek() {
		validateEmptyQueue();

		return data.peek();
	}

	public int dequeue() {
		validateEmptyQueue();

		return data.pop();
	}

	private void validateEmptyQueue() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}
	}
}
