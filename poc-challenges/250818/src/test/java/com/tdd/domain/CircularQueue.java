package com.tdd.domain;

public class CircularQueue {

	private Integer single;

	public CircularQueue(int i) {

	}

	public boolean isEmpty() {
		return single == null;
	}

	public void enqueue(int i) {
		single = i;
	}
}
