package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FibonacciTest {
    @Test
    fun fibonacci() {
        assertEquals(1, Fibonacci.fib(1))
        assertEquals(1, Fibonacci.fib(2))
        assertEquals(2, Fibonacci.fib(3))
    }
}