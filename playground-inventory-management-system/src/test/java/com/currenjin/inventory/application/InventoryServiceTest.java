package com.currenjin.inventory.application;

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
	private final Long PRODUCT_ID = 1L;
	private final Long WAREHOUSE_ID = 1L;

	private InventoryService sut;

	@Mock
	private InventoryRepository inventoryRepository;

	private final Inventory inventory = new Inventory();

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
}