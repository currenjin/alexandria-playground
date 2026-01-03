package com.currenjin.tdd

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class FibonacciTest {
    @ParameterizedTest
    @CsvSource(value = ["1,1", "2,1", "3,2", "4,3", "5,5", "6,8", "7,13", "8,21", "9,34"])
    fun fibonacci_test(actual: Int, expected: Int) {
        assertEquals(expected, Fibonacci.fib(actual))
    }
}