package com.currenjin.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final Random random = new Random();

    // Simulates 10% payment failure rate to make error traces visible in SkyWalking
    @PostMapping
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody Map<String, Object> request) {
        if (random.nextInt(10) == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Payment gateway timeout");
        }

        String transactionId = UUID.randomUUID().toString().substring(0, 12);
        return ResponseEntity.ok(Map.of("transactionId", transactionId));
    }
}
