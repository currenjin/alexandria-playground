package com.currenjin.tdd

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun testMultiplication() {
        val five = Dollar(5)

        five.times(2)

        assertEquals(10, five.amount)
    }
}