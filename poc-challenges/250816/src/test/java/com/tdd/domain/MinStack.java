package com.tdd.domain;

import java.util.NoSuchElementException;

public class MinStack {

	public void pop() {
		throw new NoSuchElementException("stack is empty");
	}
}
