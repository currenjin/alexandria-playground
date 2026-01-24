package com.currenjin.lotto

object LottoGenerator {
    fun generate(): Lotto {
        val numbers = (1..45).shuffled().take(6).map { LottoNumber(it) }
        return Lotto(numbers)
    }

    fun create(numbers: List<Int>): Lotto {
        return Lotto(numbers.map { LottoNumber(it) })
    }
}
