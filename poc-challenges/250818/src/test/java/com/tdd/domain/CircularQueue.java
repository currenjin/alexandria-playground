package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

	private final int capacity;
	private final int[] data;

	private Integer single;
	private int head = 0, tail = 0, size = 0;

	public CircularQueue(int i) {
		capacity = i;
		data = new int[capacity];
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void enqueue(int i) {
		if (isFull()) {
			throw new IllegalStateException("Queue is full");
		}

		data[tail] = i;
		tail = (tail + 1) % capacity;
		size++;
	}

	public int peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty");
		}

		return data[head];
	}

	public boolean isFull() {
		return size == capacity;
	}

	public int dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty");
		}

		return data[head];
	}
}
