package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FibonacciTest {
    @ParameterizedTest
    @CsvSource("0, 0", "1, 1", "2, 1", "3, 2", "4, 3", "5, 5")
    fun fibonacci(number: Int, expected: Int) {
        assertEquals(expected, Fibonacci.fibonacci(number))
    }
}