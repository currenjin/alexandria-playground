package com.currenjin.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Random random = new Random();

    // Simulates occasional slow responses to make latency visible in SkyWalking
    @GetMapping("/{productId}/stock")
    public Map<String, Object> checkStock(
        @PathVariable String productId,
        @RequestParam(defaultValue = "1") int quantity
    ) throws InterruptedException {
        if (random.nextInt(5) == 0) {
            Thread.sleep(300 + random.nextInt(400));
        }

        boolean available = !productId.equals("out-of-stock");
        return Map.of(
            "productId", productId,
            "available", available,
            "stock", available ? 100 : 0
        );
    }
}
