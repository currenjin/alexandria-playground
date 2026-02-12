package com.currenjin.outboxinbox.scenario.order

enum class OrderStatus { CREATED, PAID }

data class Order(
    val id: String,
    val productName: String,
    val amount: Long,
    val status: OrderStatus = OrderStatus.CREATED,
)
