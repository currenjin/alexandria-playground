package com.currenjin.archunit.hexagonal.adapter.`in`

import com.currenjin.archunit.hexagonal.application.port.`in`.CreateOrderUseCase
import com.currenjin.archunit.hexagonal.domain.Order

class OrderApiAdapter(
    private val createOrderUseCase: CreateOrderUseCase,
) {
    fun createOrder(productName: String, quantity: Int, price: Long): Order =
        createOrderUseCase.createOrder(productName, quantity, price)

    fun confirmOrder(orderId: String): Order =
        createOrderUseCase.confirmOrder(orderId)

    fun cancelOrder(orderId: String): Order =
        createOrderUseCase.cancelOrder(orderId)
}
