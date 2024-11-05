package com.tdd.fibonacci;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FibonacciTest {

    @ParameterizedTest
    @CsvSource(value = {"0, 0", "1, 1", "2, 1", "3, 2", "4, 3", "5, 5", "6, 8", "7, 13"})
    void fibonacci(int n, int result) {
        assertThat(Fibonacci.fib(n)).isEqualTo(result);
    }
}