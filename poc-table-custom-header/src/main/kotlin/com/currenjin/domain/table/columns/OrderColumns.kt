package com.currenjin.domain.table.columns

import com.currenjin.domain.order.Order
import com.currenjin.domain.table.Column

enum class OrderColumns(
    override val key: String,
    override val extractor: (Order) -> Any?,
) : Column<Order> {
    ID("id", { it.id.value }),
    BUYER_ID("buyerId", { it.buyerId?.value }),
    BUYER_NAME("buyerName", { it.buyerName }),
    RECEIVED_DATE("receivedDate", { it.receivedDate }),
    ORDER_CODE("orderCode", { it.orderCode }),
    ORDER_NUMBER("orderNumber", { it.orderNumber }),
    ;

    companion object {
        val default = entries.toList()
    }
}
