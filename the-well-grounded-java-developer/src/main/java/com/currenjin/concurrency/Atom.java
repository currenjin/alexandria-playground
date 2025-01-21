package com.currenjin.concurrency;

public class Atom {
	private volatile int value;

	public Atom(int value) {
		this.value = value;
	}

	public final int incrementAndGet() {
		while(true) {
			int current = value;
			int next = current + 1;
			if (compareAndSet(current, next)) {
				return next;
			}
		}
	}

	private boolean compareAndSet(int expected, int newValue) {
		return true;
		// CPU 명령
		// return unsafe.compareAndSwapInt(this, valueOffset, expected, newValue);
	}
}
