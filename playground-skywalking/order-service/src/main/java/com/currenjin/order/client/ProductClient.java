package com.currenjin.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product-service.url}") String productServiceUrl) {
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }

    public boolean checkStock(String productId, int quantity) {
        String url = productServiceUrl + "/products/" + productId + "/stock?quantity=" + quantity;
        StockResponse response = restTemplate.getForObject(url, StockResponse.class);
        return response != null && response.isAvailable();
    }

    public static class StockResponse {
        private boolean available;

        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
    }
}
