package com.currenjin.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PaymentClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentClient(RestTemplate restTemplate,
                         @Value("${payment-service.url}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public String processPayment(String orderId, int amount) {
        Map<String, Object> request = Map.of("orderId", orderId, "amount", amount);
        PaymentResponse response = restTemplate.postForObject(
            paymentServiceUrl + "/payments", request, PaymentResponse.class);
        return response != null ? response.getTransactionId() : null;
    }

    public static class PaymentResponse {
        private String transactionId;

        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    }
}
