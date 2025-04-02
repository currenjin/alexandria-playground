package com.currenjin.learningtest.onetomany;

import java.util.List;

public class Operator {

    @Deprecated
    public static int sum(int n) {
        return n;
    }

    public static int sum(List<Integer> numbers) {
        return numbers.stream().mapToInt(e -> e).sum();
    }
}
