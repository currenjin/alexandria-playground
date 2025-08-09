package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {

    private List<Integer> data = new ArrayList<>();

    public int top() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }

        return data.get(data.size() - 1);
    }

    public void push(int i) {
        data.add(i);
    }

    public void pop() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }

        data.remove(data.size() - 1);
    }

    public void getMin() {
        throw new NoSuchElementException("stack is empty");
    }
}
