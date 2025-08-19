package com.tdd.domain;

import java.util.NoSuchElementException;

public class CircularQueue {

    private final int[] data;
    private final int capacity;
    private int size = 0, head = 0, tail = 0;

    public CircularQueue(int i) {
        capacity = i;
        data = new int[capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(int i) {
        data[head] = i;
        head = (head + 1) % capacity;
        size++;
    }

    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }

        return data[tail];
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }

        int value = data[tail];
        tail = (tail + 1) % capacity;
        size--;

        return value;
    }
}
