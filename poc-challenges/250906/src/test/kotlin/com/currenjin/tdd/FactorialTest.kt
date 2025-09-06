package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FactorialTest {
    @Test
    fun factorial() {
        assertEquals(1, Factorial.fac(1))
        assertEquals(2, Factorial.fac(2))
        assertEquals(6, Factorial.fac(3))
    }
}