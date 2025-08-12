package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {
	private final Deque<Integer> in = new ArrayDeque<>();
	private final Deque<Integer> out = new ArrayDeque<>();

	public boolean isEmpty() {
		return in.isEmpty() && out.isEmpty();
	}

	public void enqueue(int i) {
		in.push(i);
	}

	public int peek() {
		validateEmpty();

		if (out.isEmpty()) {
			while (!in.isEmpty()) {
				out.push(in.pop());
			}
		}

		return out.peek();
	}

	public int dequeue() {
		validateEmpty();

		if (out.isEmpty()) {
			while (!in.isEmpty()) {
				out.push(in.pop());
			}
		}

		return out.pop();
	}

	private void validateEmpty() {
		if (isEmpty()) {
			throw new NoSuchElementException("Queue is empty");
		}
	}
}
