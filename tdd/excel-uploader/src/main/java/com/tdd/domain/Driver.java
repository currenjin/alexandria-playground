package com.tdd.domain;

public class Driver {
	public Driver(String name, String phoneNumber) {
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	private final String name;
	private final String phoneNumber;

	public String getName() {
		return this.name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}
}
