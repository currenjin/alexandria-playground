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
			throw new NoSuchElementException("stack is empty");
		}

		if (out.isEmpty()) {
			while (!in.isEmpty()) {
				out.push(in.pop());
			}
		}

		return out.peek();
	}

	public void dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}
	}
}
