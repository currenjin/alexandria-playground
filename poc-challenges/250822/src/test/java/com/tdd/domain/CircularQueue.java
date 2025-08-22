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
        validateFullQueue();

        this.data[head] = i;
        head = (head + 1) % capacity;
        size++;
    }

    public int peek() {
        validateEmptyQueue();

        return data[tail];
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int dequeue() {
        validateEmptyQueue();

        int value = data[tail];
        tail = (tail + 1) % capacity;
        size--;

        return value;
    }

    private void validateFullQueue() {
        if (isFull()) {
            throw new IllegalStateException("Circular Queue is full");
        }
    }

    private void validateEmptyQueue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Circular Queue is empty");
        }
    }
}
