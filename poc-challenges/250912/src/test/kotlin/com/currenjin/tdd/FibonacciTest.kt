package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FibonacciTest {
    @Test
    fun fib() {
        assertEquals(0, Fibonacci.fib(0))
        assertEquals(1, Fibonacci.fib(1))
    }
}