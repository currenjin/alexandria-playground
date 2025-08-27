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

    operator fun times(multiplier: Int): Money {
        val amount = this.amount * multiplier
        return Money(amount)
    }

    operator fun div(divisor: Int): Money {
        val amount = this.amount / divisor
        return Money(amount)
    }

    fun allocate(parts: Int): List<Money> {
        require(parts > 0)
        val base = amount / parts
        val r = amount % parts
        return (0 until parts).map {
            Money(base + if (it < r) 1 else 0)
        }
    }
}
