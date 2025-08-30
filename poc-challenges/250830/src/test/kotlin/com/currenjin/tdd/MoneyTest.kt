package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun testMultiplication() {
        val five = Dollar(5)

        assertEquals(Dollar(10), five.times(2))
        assertEquals(Dollar(15), five.times(3))
    }

    @Test
    fun testEquality() {
        assertEquals(Dollar(5), Dollar(5))
    }

    @Test
    fun testFrancMultiplication() {
        val five = Franc(5)

        assertEquals(Franc(10), five.times(2))
        assertEquals(Franc(15), five.times(3))
    }
}