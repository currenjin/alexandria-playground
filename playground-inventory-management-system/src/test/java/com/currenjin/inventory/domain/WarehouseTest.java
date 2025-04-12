package com.currenjin.inventory.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarehouseTest {

	Warehouse warehouse;

	private final String name = "warehouse";
	private final String location = "location";

	@BeforeEach
	void setUp() {
		warehouse = new Warehouse(name, location);
	}

	@Test
	void creation() {
		assertDoesNotThrow(() -> new Warehouse());
	}

	@Test
	void creation_with_parameters() {
		assertDoesNotThrow(() -> new Warehouse(
			name,
			location
		));
	}
}