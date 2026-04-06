package com.currenjin.order.dto;

public class OrderRequest {
    private String productId;
    private int quantity;

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
