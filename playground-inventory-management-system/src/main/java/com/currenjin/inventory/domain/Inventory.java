package com.currenjin.inventory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inventories")
public class Inventory {
	public static final int POSITIVE_CONDITION = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "warehouse_id", nullable = false)
	private Long warehouseId;

	@Column(name = "quantity", nullable = false)
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
