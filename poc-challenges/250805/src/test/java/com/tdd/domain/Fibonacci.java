package com.tdd.domain;

public class Fibonacci {
	public static int fib(int i) {
		if (i <= 1) return i;
		return fib(i - 2) + fib(i - 1);
	}
}
