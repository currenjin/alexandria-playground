package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircularQueueTest {
    @Test
    void isEmpty_returnsTrue() {
        CircularQueue queue = new CircularQueue(1);

        assertTrue(queue.isEmpty());
    }
}
