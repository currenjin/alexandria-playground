package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

	private final int capacity;
	private boolean empty = true;
	private final int[] data;
	private int head = 0, tail = 0, size = 0;

	public CircularQueue(int i) {
		capacity = i;
		data = new int[capacity];
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void enqueue(int i) {
		if (size == capacity) {
			throw new IllegalStateException("queue is full");
		}

		data[tail] = i;
		tail = (tail + 1) % capacity;
		size++;
	}

	public int peek() {
		validateEmptyQueue();

		return data[head];
	}

	public int dequeue() {
		validateEmptyQueue();

		int value = data[head];
		head = (head + 1) % capacity;
		size--;

		return value;
	}

	private void validateEmptyQueue() {
		if (isEmpty()) { throw new NoSuchElementException("queue is empty"); }
	}

	public boolean isFull() {
		return size == capacity;
	}
}
