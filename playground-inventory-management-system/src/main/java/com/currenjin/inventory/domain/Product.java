package com.currenjin.inventory.domain;

import java.math.BigDecimal;

public class Product {

	private String name;
	private String description;
	private BigDecimal price;

	public Product() {
	}

	public Product(String name, String description, BigDecimal price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}
}
