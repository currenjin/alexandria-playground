package com.currenjin.tdd

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals

class FactorialTest {
    @ParameterizedTest
    @CsvSource("1,1", "2,2", "3,6", "4,24", "5,120", "6,720", "7,5040")
    fun fac(input: Int, expected: Int) {
        assertEquals(expected, Factorial.cal(input))
    }
}