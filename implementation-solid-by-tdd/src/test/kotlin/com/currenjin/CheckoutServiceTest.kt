package com.currenjin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CheckoutServiceTest {
    @Test
    fun `VIP 고객은 10% 할인된다`() {
        val service = CheckoutService()
        val order = Order(listOf(Item("책", 10000, 1)), "VIP")

        val receipt = service.checkout(order)

        assertTrue(receipt.contains("9000"))
    }

    @Test
    fun `REGULAR 고객은 할인이 없다`() {
        val service = CheckoutService()
        val order = Order(listOf(Item("책", 10_000, 1)), "REGULAR")

        val receipt = service.checkout(order)

        assertTrue(receipt.contains("10000"))
    }
}
