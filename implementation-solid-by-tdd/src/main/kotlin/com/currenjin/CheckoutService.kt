package com.currenjin

class CheckoutService {
    fun checkout(order: Order): String {
        val total = order.items.sumOf { it.price * it.qty }

        val finalPrice = if (order.customerTier == "VIP") total * 0.9 else total

        return "총액: $finalPrice"
    }
}

data class Order(val items: List<Item>, val customerTier: String)
data class Item(val name: String, val price: Long, val qty: Int)
