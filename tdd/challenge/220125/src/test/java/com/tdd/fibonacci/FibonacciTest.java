package com.tdd.fibonacci;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FibonacciTest {
    @Test
    void test_fibonacci() {
        int[][] cases = {{0, 0}, {1, 1}, {2, 1}, {3, 2}, {4, 3}, {5, 5}, {6, 8}, {7, 13}};
        for (int i = 0; i < cases.length; i++) {
            assertThat(Fibonacci.fib(cases[i][0])).isEqualTo(cases[i][1]);
        }
    }
}
