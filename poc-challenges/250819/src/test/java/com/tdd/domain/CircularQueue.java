package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private Integer single;

    public CircularQueue(int i) {

    }

    public boolean isEmpty() {
        return single == null;
    }

    public void enqueue(int i) {
        single = i;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }

        return single;
    }

    public boolean isFull() {
        return false;
    }
}
