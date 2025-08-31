package com.currenjin.tdd

open class Money(
    open val amount: Int
) {
    fun times(multiplier: Int): Money {
        return Money(amount * multiplier)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Money) return false

        return amount == other.amount
    }

    override fun hashCode(): Int {
        return amount
    }
}
