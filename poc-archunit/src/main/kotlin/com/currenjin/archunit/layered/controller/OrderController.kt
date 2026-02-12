package com.currenjin.archunit.layered.controller

import com.currenjin.archunit.layered.domain.Order
import com.currenjin.archunit.layered.service.OrderService

class OrderController(
    private val orderService: OrderService,
) {
    fun createOrder(productName: String, quantity: Int, price: Long): Order =
        orderService.createOrder(productName, quantity, price)

    fun confirmOrder(orderId: String): Order =
        orderService.confirmOrder(orderId)

    fun cancelOrder(orderId: String): Order =
        orderService.cancelOrder(orderId)

    fun getOrder(orderId: String): Order? =
        orderService.findOrder(orderId)

    fun getAllOrders(): List<Order> =
        orderService.findAllOrders()
}
