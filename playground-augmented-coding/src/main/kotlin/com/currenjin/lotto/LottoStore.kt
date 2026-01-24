package com.currenjin.lotto

class LottoStore {
    fun purchase(amount: Int): List<Lotto> {
        require(amount >= LOTTO_PRICE)
        require(amount % LOTTO_PRICE == 0)
        val count = amount / LOTTO_PRICE
        return (1..count).map { LottoGenerator.generate() }
    }

    companion object {
        private const val LOTTO_PRICE = 1000
    }
}
