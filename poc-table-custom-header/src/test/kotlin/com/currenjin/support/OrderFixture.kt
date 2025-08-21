package com.currenjin.support

import com.currenjin.order.Order
import java.time.LocalDate

class OrderFixture {
    companion object {
        fun create(): Order =
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
