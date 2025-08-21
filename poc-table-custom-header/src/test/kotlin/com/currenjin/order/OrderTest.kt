package com.currenjin.order

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDate

class OrderTest {
    @Test
    fun create() {
        assertDoesNotThrow {
            Order(
                id = Order.OrderId(1L),
                buyerId = Order.BuyerId(1L),
                buyerName = "거래처",
                receivedDate = LocalDate.now(),
                orderCode = "D-12321321",
                "R-12321321",
            )
        }
    }
}
