package com.currenjin.tdd

data class Franc(
    private val amount: Int
) {
    fun times(multiplier: Int): Franc {
        return Franc(amount * multiplier)
    }
}
