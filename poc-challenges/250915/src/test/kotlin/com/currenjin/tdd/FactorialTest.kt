package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FactorialTest {
    @Test
    fun fac() {
        assertEquals(1, Factorial.cal(1))
        assertEquals(2, Factorial.cal(2))
    }
}