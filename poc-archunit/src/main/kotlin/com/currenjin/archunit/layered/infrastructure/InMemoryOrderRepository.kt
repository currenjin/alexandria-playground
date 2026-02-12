package com.currenjin.archunit.layered.infrastructure

import com.currenjin.archunit.layered.domain.Order
import com.currenjin.archunit.layered.repository.OrderRepository

class InMemoryOrderRepository : OrderRepository {
    private val storage = mutableMapOf<String, Order>()

    override fun save(order: Order): Order {
        storage[order.id] = order
        return order
    }

    override fun findById(id: String): Order? = storage[id]

    override fun findAll(): List<Order> = storage.values.toList()
}
