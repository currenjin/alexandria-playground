package com.tdd.domain;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {
	private final Deque<Integer> stack = new ArrayDeque<>();

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public void enqueue(int i) {
		stack.push(i);
	}

	public int peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("stack is empty");
		}

		return stack.peek();
	}
}
