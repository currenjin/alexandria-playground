package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun testMultiplication() {
        val fiveDollar = Money.dollar(5)
        val fiveFranc = Money.franc(5)

        assertEquals(Money(10), fiveDollar.times(2))
        assertEquals(Money(15), fiveDollar.times(3))

        assertEquals(Money(10), fiveFranc.times(2))
        assertEquals(Money(15), fiveFranc.times(3))
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