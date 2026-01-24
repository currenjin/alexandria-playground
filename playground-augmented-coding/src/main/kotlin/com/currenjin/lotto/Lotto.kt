package com.currenjin.lotto

class Lotto(numbers: List<LottoNumber>) {
    val numbers: Set<LottoNumber> = numbers.toSet()

    init {
        require(this.numbers.size == LOTTO_SIZE)
    }

    fun contains(number: LottoNumber): Boolean = numbers.contains(number)

    fun matchCount(other: Lotto): Int = numbers.intersect(other.numbers).size

    companion object {
        private const val LOTTO_SIZE = 6
    }
}
