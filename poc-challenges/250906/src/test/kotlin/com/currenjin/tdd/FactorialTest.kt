package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FactorialTest {
    @Test
    fun factorial() {
        assertEquals(1, Factorial.fac(1))
    }
}