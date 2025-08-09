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
}
