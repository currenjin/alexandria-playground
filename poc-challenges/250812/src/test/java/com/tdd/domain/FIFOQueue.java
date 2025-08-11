package com.tdd.domain;

import java.awt.*;
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

	public void peek() {
		throw new NoSuchElementException("stack is empty");
	}
}
