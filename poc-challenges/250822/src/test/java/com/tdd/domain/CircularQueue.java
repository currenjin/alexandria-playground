package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private int[] data;
    private int head = 0, tail = 0, size = 0;

    public CircularQueue(int i) {
        data = new int[i];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int i) {
        this.data[head] = i;
        head++;
        size++;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Circular Queue is empty");
        }

        return data[tail];
    }
}
