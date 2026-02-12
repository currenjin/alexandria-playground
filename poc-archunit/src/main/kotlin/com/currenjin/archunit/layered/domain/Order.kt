package com.currenjin.archunit.layered.domain

data class Order(
    val id: String,
    val productName: String,
    val quantity: Int,
    val price: Long,
    val status: OrderStatus = OrderStatus.CREATED,
) {
    fun confirm(): Order = copy(status = OrderStatus.CONFIRMED)

    fun cancel(): Order = copy(status = OrderStatus.CANCELLED)
}
