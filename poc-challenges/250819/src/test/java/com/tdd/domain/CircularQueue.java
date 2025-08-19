package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private final int capacity;
    private Integer single;
    private int size;

    public CircularQueue(int i) {
        capacity = i;
    }

    public boolean isEmpty() {
        return single == null;
    }

    public void enqueue(int i) {
        single = i;
        size++;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }

        return single;
    }

    public boolean isFull() {
        return size == capacity;
    }
}
