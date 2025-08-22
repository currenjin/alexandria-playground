package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private final int capacity;
    private final int[] data;
    private int head = 0, tail = 0, size = 0;

    public CircularQueue(int i) {
        capacity = i;
        data = new int[capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int i) {
        if (isFull()) {
            throw new IllegalStateException("Circular Queue is full");
        }

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

    public boolean isFull() {
        return size == capacity;
    }

    public void dequeue() {
        throw new NoSuchElementException("Circular Queue is empty");
    }
}
