package com.currenjin.tdd

data class Money(
    val totalAmount: Long,
) {
    companion object {
        fun of(totalAmount: Long) = Money(totalAmount)
    }
}
