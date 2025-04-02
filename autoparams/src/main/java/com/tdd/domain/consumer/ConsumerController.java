package com.tdd.domain.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    private final ConsumerJpaRepository repository;

    @Autowired
    public ConsumerController(ConsumerJpaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/api/consumer/signup")
    public ResponseEntity<Void> signUp(@RequestBody Signup command) {
        ConsumerEntity consumer = ConsumerEntity.builder().email(command.getEmail()).build();

        try {
            repository.save(consumer);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }
}
