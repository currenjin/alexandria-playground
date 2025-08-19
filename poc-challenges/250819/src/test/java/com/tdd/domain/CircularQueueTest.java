package com.tdd.domain;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircularQueueTest {

    @Test
    void isEmpty_returnTrue_whenEmpty() {
        CircularQueue queue = new CircularQueue(1);

        assertTrue(queue.isEmpty());
    }

    @Test
    void enqueue_then_isEmpty_returnFalse() {
        CircularQueue queue = new CircularQueue(1);

        queue.enqueue(1);

        assertFalse(queue.isEmpty());
    }
}
