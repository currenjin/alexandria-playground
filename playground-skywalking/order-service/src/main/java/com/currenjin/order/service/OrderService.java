package com.currenjin.order.service;

import com.currenjin.order.client.PaymentClient;
import com.currenjin.order.client.ProductClient;
import com.currenjin.order.dto.OrderRequest;
import com.currenjin.order.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(ProductClient productClient, PaymentClient paymentClient) {
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        String orderId = UUID.randomUUID().toString().substring(0, 8);

        boolean inStock = productClient.checkStock(request.getProductId(), request.getQuantity());
        if (!inStock) {
            return new OrderResponse(orderId, "FAILED", "Out of stock");
        }

        int amount = request.getQuantity() * 1000;
        String transactionId = paymentClient.processPayment(orderId, amount);

        return new OrderResponse(orderId, "COMPLETED",
            "Order placed. Transaction: " + transactionId);
    }
}
