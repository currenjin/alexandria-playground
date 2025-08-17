package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

	private boolean empty = true;

	public CircularQueue(int i) {

	}

	public boolean isEmpty() {
		return empty;
	}

	public void enqueue(int i) {
		empty = false;
	}

	public int peek() {
		if (empty) { throw new NoSuchElementException("queue is empty"); }

		return 1;
	}
}
