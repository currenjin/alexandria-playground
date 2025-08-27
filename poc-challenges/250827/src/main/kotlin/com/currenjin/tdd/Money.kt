package com.currenjin.tdd

data class Money private constructor(
    val amount: Long,
) : Comparable<Money> {
    init {
        require(amount >= 0)
    }

    override fun compareTo(o: Money) = amount.compareTo(o.amount)

    operator fun plus(o: Money) = of(amount + o.amount)

    operator fun times(n: Int) = of(amount * n)

    companion object {
        fun of(v: Long) = Money(v)

        fun zero() = Money(0)
    }
}
