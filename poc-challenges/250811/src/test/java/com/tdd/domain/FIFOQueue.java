package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {
	private final Deque<Integer> in = new ArrayDeque<>();
	private final Deque<Integer> out = new ArrayDeque<>();

	public boolean isEmpty() {
		return in.isEmpty();
	}

	public void enqueue(int i) {
		in.push(i);
	}

	public int peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}

		if (out.isEmpty()) {
			out.push(in.pop());
		}

		return out.pop();
	}

	public void dequeue() {
		throw new NoSuchElementException("queue is empty");
	}
}
