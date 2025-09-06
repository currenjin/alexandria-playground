package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FactorialTest {
    @ParameterizedTest
    @CsvSource("1, 1", "2, 2", "3, 6", "4, 24", "5, 120", "6, 720")
    fun factorial(input: Int, expected: Int) {
        assertEquals(expected, Factorial.fac(input))
    }
}