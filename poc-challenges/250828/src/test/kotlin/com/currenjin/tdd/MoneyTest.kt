package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MoneyTest {
    @Test
    fun add_two_amounts() {
        val three = Money.of(3000)
        val five = Money.of(5000)

        val actual = five + three

        assertEquals(Money.of(8000), actual)
    }

    @Test
    fun subtract_two_amounts() {
        val three = Money.of(3000)
        val five = Money.of(5000)

        val actual = five - three

        assertEquals(Money.of(2000), actual)
    }

    @Test
    fun money_cannot_be_negative() {
        assertThrows<IllegalArgumentException> {
            Money.of(-1)
        }
    }
}
