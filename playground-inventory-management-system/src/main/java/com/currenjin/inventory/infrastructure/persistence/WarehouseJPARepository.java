package com.currenjin.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currenjin.inventory.domain.Warehouse;
import com.currenjin.inventory.domain.repository.WarehouseRepository;

public interface WarehouseJPARepository extends WarehouseRepository, JpaRepository<Warehouse, Long> {
}
