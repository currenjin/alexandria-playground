package com.tdd.fibonacci;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FibonacciTest {
    @Test
    void testFibonacci() {
        int cases[][] = {{0, 0}, {1, 1}, {2, 1}, {3, 2}};
        for (int i=0; i < cases.length; i++) {
            assertThat(Fibonacci.fib(cases[i][0])).isEqualTo(cases[i][1]);
        }
    }
}
