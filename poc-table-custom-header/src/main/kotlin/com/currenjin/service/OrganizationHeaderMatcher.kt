package com.currenjin.service

import com.currenjin.order.Order

class OrganizationHeaderMatcher(
    organizationCustomHeaderReader: OrganizationCustomHeaderReader,
) {
    fun matchBy(
        organizationId: Long,
        tableName: String,
        order: Order,
    ): Map<String, Any?> =
        mapOf(
            "id" to order.id,
            "buyerId" to order.buyerId,
            "buyerName" to order.buyerName,
            "receivedDate" to order.receivedDate,
            "orderCode" to order.orderCode,
            "orderNumber" to order.orderNumber,
        )
}
