package com.currenjin

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CheckoutServiceTest {
    @Test
    fun `VIP 고객은 10% 할인된다`() {
        val service = CheckoutService()
        val order = Order(listOf(Item("책", 10000.0, 1)), "VIP")

        val receipt = service.checkout(order)

        assertTrue(receipt.contains("9000.0"))
    }
}
