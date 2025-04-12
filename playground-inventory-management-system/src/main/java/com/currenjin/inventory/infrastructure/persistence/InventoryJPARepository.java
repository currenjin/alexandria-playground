package com.currenjin.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.inventory.domain.Inventory;
import com.currenjin.inventory.domain.repository.InventoryRepository;

public interface InventoryJPARepository extends InventoryRepository, JpaRepository<Inventory, Long> {
}
