package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MoneyTest {
    @Test
    fun add_two_amounts() {
        val three = Money.of(3000)
        val five = Money.of(5000)

        val actual = five + three

        assertEquals(Money.of(8000), actual)
    }
}
