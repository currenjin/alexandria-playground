package com.currenjin.tdd

data class LineItem(
    val unit: Money,
    val qty: Qty,
) {
    fun total(): Money = unit * qty.value
}
