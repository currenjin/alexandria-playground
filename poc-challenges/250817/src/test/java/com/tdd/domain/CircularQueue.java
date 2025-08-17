package com.tdd.domain;

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
}
