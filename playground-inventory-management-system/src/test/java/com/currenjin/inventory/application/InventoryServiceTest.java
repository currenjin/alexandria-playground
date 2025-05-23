package com.currenjin.inventory.application;

import static com.currenjin.inventory.TestSupport.AMOUNT;
import static com.currenjin.inventory.TestSupport.PRODUCT_ID;
import static com.currenjin.inventory.TestSupport.WAREHOUSE_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.currenjin.inventory.domain.Inventory;
import com.currenjin.inventory.domain.repository.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
	private InventoryService sut;

	@Mock
	private InventoryRepository inventoryRepository;

	@Mock
	private Inventory inventory;

	@BeforeEach
	void setUp() {
		sut = new InventoryService(inventoryRepository);
	}

	@Test
	void find_inventory() {
		when(inventoryRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID))
			.thenReturn(Optional.of(inventory));

		sut.findInventory(PRODUCT_ID, WAREHOUSE_ID);

		verify(inventoryRepository).findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID);
	}

	@Test
	void should_throw_IllegalArgumentException_when_notFoundInventoryByProductIdAndWarehouseId() {
		when(inventoryRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID))
			.thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> sut.findInventory(PRODUCT_ID, WAREHOUSE_ID));
	}

	@Test
	void increase_inventory() {
		when(inventoryRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID))
			.thenReturn(Optional.of(inventory));

		sut.increaseStock(PRODUCT_ID, WAREHOUSE_ID, AMOUNT);

		verify(inventoryRepository).findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID);
		verify(inventory).increaseStock(AMOUNT);
	}

	@Test
	void decrease_inventory() {
		when(inventoryRepository.findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID))
			.thenReturn(Optional.of(inventory));

		sut.decreaseStock(PRODUCT_ID, WAREHOUSE_ID, AMOUNT);

		verify(inventoryRepository).findByProductIdAndWarehouseId(PRODUCT_ID, WAREHOUSE_ID);
		verify(inventory).decreaseStock(AMOUNT);
	}

	@Test
	void find_inventories_by_productId() {
		sut.findInventoriesByProductId(PRODUCT_ID);

		verify(inventoryRepository).findAllByProductId(PRODUCT_ID);
	}

	@Test
	void find_inventories_by_warehouseId() {
		sut.findInventoriesByWarehouseId(WAREHOUSE_ID);

		verify(inventoryRepository).findAllByWarehouseId(WAREHOUSE_ID);
	}
}