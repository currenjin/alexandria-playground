package com.currenjin.archunit.layered.repository

import com.currenjin.archunit.layered.domain.Order

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: String): Order?
    fun findAll(): List<Order>
}
