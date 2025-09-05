package com.currenjin.tdd

import kotlin.test.Test
import kotlin.test.assertEquals

class FibonacciTest {
    @Test
    fun fibonacci_test() {
        assertEquals(0, Fibonacci.fib(0))
    }
}