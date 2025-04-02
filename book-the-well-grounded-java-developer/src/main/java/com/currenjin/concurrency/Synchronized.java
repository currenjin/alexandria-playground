package com.currenjin.concurrency;

public class Synchronized {
	private int balance;

	public synchronized boolean withdraw(int amount) {
		if (balance >= amount) {
			balance = balance - amount;
			return true;
		}

		return false;
	}
}
