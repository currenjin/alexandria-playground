package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private Integer data;

    public CircularQueue(int i) {

    }

    public boolean isEmpty() {
        return data == null;
    }

    public void enqueue(int i) {
        this.data = i;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Circular Queue is empty");
        }

        return data;
    }
}
