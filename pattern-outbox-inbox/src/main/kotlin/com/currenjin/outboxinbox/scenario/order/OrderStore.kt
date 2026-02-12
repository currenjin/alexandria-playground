package com.currenjin.outboxinbox.scenario.order

interface OrderStore {
    fun save(order: Order)
    fun findById(id: String): Order?
    fun findAll(): List<Order>
}
