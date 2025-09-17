package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FactorialTest {
    @ParameterizedTest
    @CsvSource("1, 1")
    fun factorial(expected: Int, input: Int) {
        assertEquals(expected, Factorial.fac(input))
    }
}