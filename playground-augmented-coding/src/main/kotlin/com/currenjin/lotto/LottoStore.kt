package com.currenjin.lotto

class LottoStore {
    fun purchase(amount: Int): List<Lotto> {
        return listOf(LottoGenerator.generate())
    }
}
