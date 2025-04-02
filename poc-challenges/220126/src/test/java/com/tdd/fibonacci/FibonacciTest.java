package com.tdd.fibonacci;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FibonacciTest {
    @ParameterizedTest
    @CsvSource(value={"0, 0", "1, 1", "2, 1", "3, 2", "4, 3", "5, 5"})
    void fibonacci(int input, int output) {
        assertThat(Fibonacci.fib(input)).isEqualTo(output);
    }
}
