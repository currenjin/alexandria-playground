package com.tdd.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FibonacciTest {
    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,1", "1,2", "2,3"})
    void fibonacci(int result, int number) {
        assertEquals(result, Fibonacci.fib(number));
    }
}
