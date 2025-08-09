package com.tdd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinStack {

    private List<Integer> data = new ArrayList<>();
    private List<Integer> minStack = new ArrayList<>();

    public int top() {
        validateEmpty();

        return data.get(data.size() - 1);
    }

    public void push(int i) {
        data.add(i);

        int min = minStack.isEmpty()
                ? i
                : Math.min(i, minStack.get(minStack.size() - 1));
        minStack.add(min);
    }

    public void pop() {
        validateEmpty();

        data.remove(data.size() - 1);
    }

    public int getMin() {
        validateEmpty();

        return minStack.get(minStack.size() - 1);
    }

    private void validateEmpty() {
        if (data.isEmpty()) {
            throw new NoSuchElementException("stack is empty");
        }
    }
}
