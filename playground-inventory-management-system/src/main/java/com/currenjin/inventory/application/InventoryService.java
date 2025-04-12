package com.currenjin.inventory.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.currenjin.inventory.domain.Inventory;
import com.currenjin.inventory.domain.repository.InventoryRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	@Transactional(readOnly = true)
	public Inventory findInventory(Long productId, Long warehouseId) {
		return inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
			.orElseThrow(() -> new IllegalArgumentException("Inventory not found. Product id: " + productId + ", warehouse id: " + warehouseId));
	}

	@Transactional
	public void increaseStock(Long productId, Long warehouseId, int amount) {
		Inventory inventory = findInventory(productId, warehouseId);
		inventory.increaseStock(amount);
	}
}
