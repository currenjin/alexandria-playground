package com.currenjin.tdd

data class Money private constructor(
    val amount: Long,
) {
    companion object {
        fun of(amount: Long) = Money(amount)
    }

    init {
        require(amount >= 0) { "Money must be zero or positive" }
    }

    operator fun plus(other: Money): Money {
        val amount = this.amount + other.amount
        return Money(amount)
    }

    operator fun minus(other: Money): Money {
        val amount = this.amount - other.amount
        return Money(amount)
    }
}
