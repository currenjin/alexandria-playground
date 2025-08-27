package com.currenjin.tdd

class Qty(
    val value: Int,
) {
    init {
        require(value > 0)
    }
}
