package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun testMultiplication() {
        val five = Dollar(5)

        var product: Dollar = five.times(2)

        assertEquals(10, product.amount)

        product = five.times(3)

        assertEquals(15, product.amount)
    }
}