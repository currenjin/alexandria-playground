package com.currenjin.inventory.domain.repository;

import java.util.Optional;

import com.currenjin.inventory.domain.Inventory;

public interface InventoryRepository {
	Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}
