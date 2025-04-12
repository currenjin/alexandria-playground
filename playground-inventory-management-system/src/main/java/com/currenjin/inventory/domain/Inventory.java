package com.currenjin.inventory.domain;

public class Inventory {
	public static final int POSITIVE_CONDITION = 1;

	private Long productId;
	private Long warehouseId;
	private Integer quantity;

	public Inventory(Long productId, Long warehouseId, Integer quantity) {
		this.productId = productId;
		this.warehouseId = warehouseId;
		this.quantity = quantity;
	}

	public Inventory() {
	}

	public void increaseStock(int amount) {
		if (amount < POSITIVE_CONDITION) {
			throw new IllegalArgumentException("Amount must be positive");
		}

		this.quantity += amount;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void decreaseStock(int amount) {
		if (amount < POSITIVE_CONDITION) {
			throw new IllegalArgumentException("Amount must be positive");
		}

		if (this.quantity < amount) {
			throw new IllegalStateException("Not enough stock available");
		}

		this.quantity -= amount;
	}
}
