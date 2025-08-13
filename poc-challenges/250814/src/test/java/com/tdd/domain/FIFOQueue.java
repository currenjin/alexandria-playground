package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {

	private final Deque<Integer> in = new ArrayDeque<>();
	private final Deque<Integer> out = new ArrayDeque<>();

	public boolean isEmpty() {
		return in.isEmpty() & out.isEmpty();
	}

	public void enqueue(int i) {
		in.push(i);
	}

	public int peek() {
		validateEmptyQueue();

		moveInOrder();

		return out.peek();
	}

	public int dequeue() {
		validateEmptyQueue();

		moveInOrder();

		return out.pop();
	}

	private void moveInOrder() {
		if (out.isEmpty()) {
			while (!in.isEmpty()) {
				out.push(in.pop());
			}
		}
	}

	private void validateEmptyQueue() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}
	}
}
