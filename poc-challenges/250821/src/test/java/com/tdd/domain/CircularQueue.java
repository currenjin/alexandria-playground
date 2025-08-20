package com.tdd.domain;

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
}
