package com.currenjin.order.dto;

public class OrderResponse {
    private String orderId;
    private String status;
    private String message;

    public OrderResponse(String orderId, String status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }

    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
