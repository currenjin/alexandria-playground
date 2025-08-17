package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

	private boolean empty = true;
	private int single;

	public CircularQueue(int i) {

	}

	public boolean isEmpty() {
		return empty;
	}

	public void enqueue(int i) {
		empty = false;
		single = i;
	}

	public int peek() {
		validateEmptyQueue();

		return single;
	}

	public void dequeue() {
		validateEmptyQueue();
	}

	private void validateEmptyQueue() {
		if (empty) { throw new NoSuchElementException("queue is empty"); }
	}
}
