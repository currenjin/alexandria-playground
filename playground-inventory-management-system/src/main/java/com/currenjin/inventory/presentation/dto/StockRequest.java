package com.currenjin.inventory.presentation.dto;

public class StockRequest {
    private Integer amount;

    public StockRequest() {
    }

    public StockRequest(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}