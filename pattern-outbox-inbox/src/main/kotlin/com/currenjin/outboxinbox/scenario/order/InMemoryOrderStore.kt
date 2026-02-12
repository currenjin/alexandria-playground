package com.currenjin.outboxinbox.scenario.order

class InMemoryOrderStore : OrderStore {
    private val orders = mutableMapOf<String, Order>()

    override fun save(order: Order) {
        orders[order.id] = order
    }

    override fun findById(id: String): Order? = orders[id]

    override fun findAll(): List<Order> = orders.values.toList()
}
