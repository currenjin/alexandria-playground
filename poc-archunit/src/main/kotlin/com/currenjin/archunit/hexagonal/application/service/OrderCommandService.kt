package com.currenjin.archunit.hexagonal.application.service

import com.currenjin.archunit.hexagonal.application.port.`in`.CreateOrderUseCase
import com.currenjin.archunit.hexagonal.application.port.out.OrderPersistencePort
import com.currenjin.archunit.hexagonal.domain.Order
import java.util.UUID

class OrderCommandService(
    private val orderPersistencePort: OrderPersistencePort,
) : CreateOrderUseCase {

    override fun createOrder(productName: String, quantity: Int, price: Long): Order {
        val order = Order(
            id = UUID.randomUUID().toString(),
            productName = productName,
            quantity = quantity,
            price = price,
        )
        return orderPersistencePort.save(order)
    }

    override fun confirmOrder(orderId: String): Order {
        val order = orderPersistencePort.findById(orderId)
            ?: throw IllegalArgumentException("주문을 찾을 수 없습니다: $orderId")
        return orderPersistencePort.save(order.confirm())
    }

    override fun cancelOrder(orderId: String): Order {
        val order = orderPersistencePort.findById(orderId)
            ?: throw IllegalArgumentException("주문을 찾을 수 없습니다: $orderId")
        return orderPersistencePort.save(order.cancel())
    }
}
