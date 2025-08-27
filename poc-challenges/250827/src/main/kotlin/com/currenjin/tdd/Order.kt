package com.currenjin.tdd

class Order {
    private val items = mutableListOf<LineItem>()

    fun add(
        unit: Money,
        qty: Qty,
    ) {
        items += LineItem(unit, qty)
    }

    fun subtotal(): Money = items.fold(Money.zero()) { acc, it -> acc + it.total() }
}
