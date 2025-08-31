package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun testMultiplication() {
        val five = Money(5)

        assertEquals(Money(10), five.times(2))
        assertEquals(Money(15), five.times(3))
    }

    @Test
    fun testEquality() {
        assertEquals(Money(5), Money(5))
        assertEquals(Money(10), Money(10))
        assertEquals(Dollar(5), Dollar(5))
        assertEquals<Money>(Dollar(5), Franc(5))
    }

    @Test
    fun testFrancMultiplication() {
        val five = Money(5)

        assertEquals(Money(10), five.times(2))
        assertEquals(Money(15), five.times(3))
    }
}