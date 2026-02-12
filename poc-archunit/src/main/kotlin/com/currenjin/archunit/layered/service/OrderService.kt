package com.currenjin.archunit.layered.service

import com.currenjin.archunit.layered.domain.Order
import com.currenjin.archunit.layered.repository.OrderRepository
import java.util.UUID

class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun createOrder(productName: String, quantity: Int, price: Long): Order {
        val order = Order(
            id = UUID.randomUUID().toString(),
            productName = productName,
            quantity = quantity,
            price = price,
        )
        return orderRepository.save(order)
    }

    fun confirmOrder(orderId: String): Order {
        val order = orderRepository.findById(orderId)
            ?: throw IllegalArgumentException("주문을 찾을 수 없습니다: $orderId")
        return orderRepository.save(order.confirm())
    }

    fun cancelOrder(orderId: String): Order {
        val order = orderRepository.findById(orderId)
            ?: throw IllegalArgumentException("주문을 찾을 수 없습니다: $orderId")
        return orderRepository.save(order.cancel())
    }

    fun findOrder(orderId: String): Order? = orderRepository.findById(orderId)

    fun findAllOrders(): List<Order> = orderRepository.findAll()
}
