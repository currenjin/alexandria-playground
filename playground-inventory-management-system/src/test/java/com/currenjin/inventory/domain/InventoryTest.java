package com.currenjin.inventory.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTest {
	Inventory inventory;

	Long productId = 1L;
	Long warehouseId = 1L;
	Integer quantity = 100;

	@BeforeEach
	void setUp() {
		inventory = new Inventory(productId, warehouseId, quantity);
	}

	@Test
	void creation() {
		assertDoesNotThrow(() -> new Inventory());
	}

	@Test
	void creation_with_parameters() {
		assertDoesNotThrow(() -> new Inventory(
			productId,
			warehouseId,
			quantity
		));
	}

	@Test
	void increasingStockWithPositiveAmount() {
		Integer beforeQuantity = inventory.getQuantity();

		inventory.increaseStock(1);

		Integer afterQuantity = inventory.getQuantity();
		assertEquals(beforeQuantity + 1, afterQuantity);
	}

	@Test
	void should_throw_IllegalArgumentException_when_increasingStockWithNegativeAmount() {
		assertThrows(IllegalArgumentException.class, () -> inventory.increaseStock(-1));
	}

	@Test
	void should_throw_IllegalArgumentException_when_increasingStockWithZeroAmount() {
		assertThrows(IllegalArgumentException.class, () -> inventory.increaseStock(0));
	}

	@Test
	void decreasingStockWithPositiveAmount() {
		Integer beforeQuantity = inventory.getQuantity();

		inventory.decreaseStock(1);

		Integer afterQuantity = inventory.getQuantity();
		assertEquals(beforeQuantity - 1, afterQuantity);
	}

	@Test
	void should_throw_IllegalArgumentException_when_decreasingStockWithNegativeAmount() {
		assertThrows(IllegalArgumentException.class, () -> inventory.decreaseStock(-1));
	}

	@Test
	void should_throw_IllegalArgumentException_when_decreasingStockWithZeroAmount() {
		assertThrows(IllegalArgumentException.class, () -> inventory.decreaseStock(0));
	}

	@Test
	void should_throw_IllegalStateException_when_amountExceedsAvailableQuantity() {
		assertThrows(IllegalStateException.class, () -> inventory.decreaseStock(inventory.getQuantity() + 1));
	}
}