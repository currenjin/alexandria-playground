package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoneyTest {
    @Test
    fun testMultiplication() {
        val five = Dollar(5)

        var product: Dollar = five.times(2)

        assertEquals(10, product.amount)

        product = five.times(3)

        assertEquals(15, product.amount)
    }

    @Test
    fun testEquality() {
        assertEquals(Dollar(5), Dollar(5))
    }
}