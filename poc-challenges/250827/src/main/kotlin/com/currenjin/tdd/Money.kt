package com.currenjin.tdd

data class Money private constructor(
    val amount: Long,
) : Comparable<Money> {
    init {
        require(amount >= 0)
    }

    override fun compareTo(other: Money) = amount.compareTo(other.amount)

    companion object {
        fun of(v: Long) = Money(v)

        fun zero() = Money(0)
    }
}
