package com.currenjin.tdd

class Order {
    private val items = mutableListOf<LineItem>()

    fun add(item: LineItem) {
        items += item
    }

    fun subtotal(): Money = items.fold(Money.of(0)) { acc, it -> acc + it.total() }
}
