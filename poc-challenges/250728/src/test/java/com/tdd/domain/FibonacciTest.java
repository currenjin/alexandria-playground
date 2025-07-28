package com.tdd.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciTest {
    @Test
    void fibonacci_test() {
        assertEquals(0, Fibonacci.fib(0));
        assertEquals(1, Fibonacci.fib(1));
    }
}
