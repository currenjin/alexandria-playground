package com.currenjin.archunit.hexagonal.application.port.`in`

import com.currenjin.archunit.hexagonal.domain.Order

interface CreateOrderUseCase {
    fun createOrder(productName: String, quantity: Int, price: Long): Order
    fun confirmOrder(orderId: String): Order
    fun cancelOrder(orderId: String): Order
}
