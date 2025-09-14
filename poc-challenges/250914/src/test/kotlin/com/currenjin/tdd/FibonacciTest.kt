package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FibonacciTest {
    @Test
    fun fibonacci() {
        assertEquals(0, Fibonacci.fib(0))
    }
}