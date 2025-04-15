package com.currenjin.inventory.presentation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.currenjin.inventory.application.InventoryService;
import com.currenjin.inventory.domain.Inventory;
import com.currenjin.inventory.presentation.dto.InventoryResponse;
import com.currenjin.inventory.presentation.dto.StockRequest;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryResponse>> getInventoriesByProductId(@PathVariable Long productId) {
        List<Inventory> inventories = inventoryService.findInventoriesByProductId(productId);
        List<InventoryResponse> response = inventories.stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<InventoryResponse>> getInventoriesByWarehouseId(@PathVariable Long warehouseId) {
        List<Inventory> inventories = inventoryService.findInventoriesByWarehouseId(warehouseId);
        List<InventoryResponse> response = inventories.stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId,
            @PathVariable Long warehouseId) {
        Inventory inventory = inventoryService.findInventory(productId, warehouseId);
        return ResponseEntity.ok(InventoryResponse.from(inventory));
    }

    @PostMapping("/product/{productId}/warehouse/{warehouseId}/increase")
    public ResponseEntity<Void> increaseStock(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRequest request) {
        inventoryService.increaseStock(productId, warehouseId, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/{productId}/warehouse/{warehouseId}/decrease")
    public ResponseEntity<Void> decreaseStock(
            @PathVariable Long productId,
            @PathVariable Long warehouseId,
            @RequestBody StockRequest request) {
        inventoryService.decreaseStock(productId, warehouseId, request.getAmount());
        return ResponseEntity.ok().build();
    }
}
