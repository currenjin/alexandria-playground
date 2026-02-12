package com.currenjin.outboxinbox.scenario.order

import com.currenjin.outboxinbox.core.OutboxRecord
import com.currenjin.outboxinbox.core.TransactionalOutbox

class OrderService(
    private val orderStore: OrderStore,
    private val transactionalOutbox: TransactionalOutbox,
) {
    val log get() = transactionalOutbox.log

    fun createOrder(orderId: String, productName: String, amount: Long): Order {
        val order = Order(id = orderId, productName = productName, amount = amount)
        val event = OrderCreatedEvent(orderId, productName, amount)
        val outboxRecord = OutboxRecord(
            aggregateType = "Order",
            aggregateId = orderId,
            eventType = OrderCreatedEvent.EVENT_TYPE,
            payload = event.toPayload(),
        )

        return transactionalOutbox.executeWithOutbox(outboxRecord) {
            orderStore.save(order)
            order
        }
    }
}
