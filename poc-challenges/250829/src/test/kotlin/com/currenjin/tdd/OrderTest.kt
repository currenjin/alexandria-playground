package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderTest {
    @Test
    fun order_subtotal_is_sum_of_line_item_totals() {
        val order = Order()
        order.add(LineItem(price = Money.of(3000), quantity = 2))
        order.add(LineItem(price = Money.of(5000), quantity = 1))

        assertEquals(Money.of(11000), order.subtotal())
    }
}
