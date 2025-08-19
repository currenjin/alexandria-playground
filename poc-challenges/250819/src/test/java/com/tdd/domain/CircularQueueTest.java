package com.tdd.domain;

import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CircularQueueTest {

    @Test
    void isEmpty_returnsTrue_whenEmpty() {
        CircularQueue queue = new CircularQueue(1);

        assertTrue(queue.isEmpty());
    }

    @Test
    void enqueue_then_isEmpty_returnsFalse() {
        CircularQueue queue = new CircularQueue(1);

        queue.enqueue(1);

        assertFalse(queue.isEmpty());
    }

    @Test
    void peek_throwsException_whenEmpty() {
        CircularQueue queue = new CircularQueue(1);

        assertThrows(NoSuchElementException.class, queue::peek);
    }

    @Test
    void enqueue_then_peek_returnsValue() {
        CircularQueue queue = new CircularQueue(1);

        queue.enqueue(1);

        assertEquals(1, queue.peek());
    }

    @Test
    void isFull_returnsFalse() {
        CircularQueue queue = new CircularQueue(1);

        assertFalse(queue.isFull());
    }

    @Test
    void enqueue_then_isFull_returnsTrue() {
        CircularQueue queue = new CircularQueue(3);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertTrue(queue.isFull());
    }

    @Test
    void enqueue_multipleValues_then_peek_returnsHeadValue() {
        CircularQueue queue = new CircularQueue(3);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.peek());
    }

    @Test
    void dequeue_throwsException_whenEmpty() {
        CircularQueue queue = new CircularQueue(1);

        assertThrows(NoSuchElementException.class, queue::dequeue);
    }
}
