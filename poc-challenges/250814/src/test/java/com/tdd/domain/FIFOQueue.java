package com.tdd.domain;

import org.junit.jupiter.api.function.Executable;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class FIFOQueue {

	private final Deque<Integer> in = new ArrayDeque<>();

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

		return in.getLast();
	}

	public int dequeue() {
		if (isEmpty()) {
			throw new NoSuchElementException("queue is empty");
		}

		return in.pop();
	}
}
