package com.currenjin.tdd

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class FactorialTest {
    @ParameterizedTest
    @CsvSource(value = ["1,1", "2,2", "3,6"])
    fun factorial_test(input: Int, expected: Int) {
        assertEquals(expected, Factorial.fac(input))
    }
}