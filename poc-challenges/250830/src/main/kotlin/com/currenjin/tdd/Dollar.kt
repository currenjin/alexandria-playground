package com.currenjin.tdd

data class Dollar(
    val amount: Int
) {
    fun times(multiplier: Int): Dollar {
        return Dollar(amount * multiplier)
    }
}
