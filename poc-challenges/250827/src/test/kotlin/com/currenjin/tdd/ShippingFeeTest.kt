package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShippingFeeTest {
    @Test
    fun order_under_threshold_pays_shipping_fee() {
        val order = Order(totalAmount = Money.of(30000))

        val shipping = ShippingFeePolicy().calculate(order)

        assertEquals(Money.of(3000), shipping)
    }
}
