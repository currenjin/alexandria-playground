package com.currenjin.inventory.presentation.dto;

import com.currenjin.inventory.domain.Inventory;

public class InventoryResponse {
    private Long productId;
    private Long warehouseId;
    private Integer quantity;

    public InventoryResponse(Long productId, Long warehouseId, Integer quantity) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }

    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(
                inventory.getProductId(),
                inventory.getWarehouseId(),
                inventory.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}