package com.currenjin.inventory.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

	Product product;

	private final String name = "product";
	private final String description = "description";
	private final BigDecimal price = BigDecimal.ONE;

	@BeforeEach
	void setUp() {
		product = new Product(name, description, price);
	}

	@Test
	void creation() {
		assertDoesNotThrow(() -> new Product());
	}

	@Test
	void creation_with_parameters() {
		assertDoesNotThrow(() -> new Product(
			name,
			description,
			price
		));
	}
}