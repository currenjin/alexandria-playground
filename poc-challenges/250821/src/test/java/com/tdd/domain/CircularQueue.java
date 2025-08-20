package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {
	private Integer data;

	public CircularQueue(int i) {

	}

	public boolean isEmpty() {
		return data == null;
	}

	public void enqueue(int i) {
		data = i;
	}

	public void peek() {
		throw new NoSuchElementException("queue is empty");
	}
}
