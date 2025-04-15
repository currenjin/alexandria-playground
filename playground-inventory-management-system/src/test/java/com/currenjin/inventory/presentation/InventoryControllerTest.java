package com.currenjin.inventory.presentation;

import static com.currenjin.inventory.TestSupport.AMOUNT;
import static com.currenjin.inventory.TestSupport.PRODUCT_ID;
import static com.currenjin.inventory.TestSupport.WAREHOUSE_ID;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.currenjin.inventory.application.InventoryService;
import com.currenjin.inventory.domain.Inventory;
import com.currenjin.inventory.presentation.dto.StockRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    void get_inventory_by_product_and_warehouse_id() throws Exception {
        Inventory inventory = new Inventory(PRODUCT_ID, WAREHOUSE_ID, 10);
        
        when(inventoryService.findInventory(PRODUCT_ID, WAREHOUSE_ID)).thenReturn(inventory);

        mockMvc.perform(get("/api/inventories/product/{productId}/warehouse/{warehouseId}", PRODUCT_ID, WAREHOUSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$.warehouseId").value(WAREHOUSE_ID))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void get_inventories_by_product_id() throws Exception {
        Inventory inventory1 = new Inventory(PRODUCT_ID, 1L, 10);
        Inventory inventory2 = new Inventory(PRODUCT_ID, 2L, 20);
        List<Inventory> inventories = Arrays.asList(inventory1, inventory2);
        
        when(inventoryService.findInventoriesByProductId(PRODUCT_ID)).thenReturn(inventories);

        mockMvc.perform(get("/api/inventories/product/{productId}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$[0].warehouseId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].productId").value(PRODUCT_ID))
                .andExpect(jsonPath("$[1].warehouseId").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(20));
    }

    @Test
    void get_inventories_by_warehouse_id() throws Exception {
        Inventory inventory1 = new Inventory(1L, WAREHOUSE_ID, 10);
        Inventory inventory2 = new Inventory(2L, WAREHOUSE_ID, 20);
        List<Inventory> inventories = Arrays.asList(inventory1, inventory2);
        
        when(inventoryService.findInventoriesByWarehouseId(WAREHOUSE_ID)).thenReturn(inventories);

        mockMvc.perform(get("/api/inventories/warehouse/{warehouseId}", WAREHOUSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].warehouseId").value(WAREHOUSE_ID))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].productId").value(2L))
                .andExpect(jsonPath("$[1].warehouseId").value(WAREHOUSE_ID))
                .andExpect(jsonPath("$[1].quantity").value(20));
    }

    @Test
    void increase_stock() throws Exception {
        StockRequest request = new StockRequest(AMOUNT);
        
        doNothing().when(inventoryService).increaseStock(PRODUCT_ID, WAREHOUSE_ID, AMOUNT);

        mockMvc.perform(post("/api/inventories/product/{productId}/warehouse/{warehouseId}/increase", PRODUCT_ID, WAREHOUSE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void decrease_stock() throws Exception {
        StockRequest request = new StockRequest(AMOUNT);
        
        doNothing().when(inventoryService).decreaseStock(PRODUCT_ID, WAREHOUSE_ID, AMOUNT);

        mockMvc.perform(post("/api/inventories/product/{productId}/warehouse/{warehouseId}/decrease", PRODUCT_ID, WAREHOUSE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}