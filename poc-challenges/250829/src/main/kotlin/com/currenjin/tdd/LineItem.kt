package com.currenjin.tdd

class LineItem(
    val price: Money,
    val quantity: Int,
) {
    fun total(): Money = price * quantity
}
