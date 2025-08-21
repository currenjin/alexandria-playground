package com.currenjin.domain.order

import java.time.LocalDate

data class Order(
    val id: OrderId,
    var buyerId: BuyerId? = null,
    var buyerName: String? = null,
    var receivedDate: LocalDate? = null,
    var orderCode: String? = null,
    var orderNumber: String? = null,
) {
    data class OrderId(
        val value: Long,
    ) {
        init {
            require(value > 0)
        }
    }

    data class BuyerId(
        val value: Long,
    ) {
        init {
            require(value > 0)
        }
    }
}
