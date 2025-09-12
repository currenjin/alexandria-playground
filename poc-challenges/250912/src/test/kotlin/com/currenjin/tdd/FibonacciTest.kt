package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FibonacciTest {
    @ParameterizedTest
    @CsvSource("0,0", "1,1")
    fun fib(number: Int, expected: Int) {
        assertEquals(expected, Fibonacci.fib(number))
    }
}