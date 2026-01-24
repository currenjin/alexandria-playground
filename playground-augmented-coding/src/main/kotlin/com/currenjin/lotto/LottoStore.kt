package com.currenjin.lotto

class LottoStore {
    fun purchase(amount: Int): List<Lotto> {
        require(amount >= LOTTO_PRICE)
        return listOf(LottoGenerator.generate())
    }

    companion object {
        private const val LOTTO_PRICE = 1000
    }
}
