package com.currenjin.tdd

data class Money private constructor(
    val amount: Long,
) {
    operator fun plus(other: Money): Money {
        val amount = this.amount + other.amount
        return Money(amount)
    }

    operator fun minus(other: Money): Money {
        val amount = this.amount - other.amount
        return Money(amount)
    }

    companion object {
        fun of(amount: Long) = Money(amount)
    }
}
