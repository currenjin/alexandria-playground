package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FactorialTest {
    @Test
    fun factorial() {
        assertEquals(1, Factorial.fac(1))
        assertEquals(2, Factorial.fac(2))
    }
}