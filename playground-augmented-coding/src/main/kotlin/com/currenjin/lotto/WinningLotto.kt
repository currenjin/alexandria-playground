package com.currenjin.lotto

class WinningLotto(val lotto: Lotto, val bonus: LottoNumber) {
    init {
        require(!lotto.contains(bonus))
    }

    fun matchCount(userLotto: Lotto): Int = lotto.matchCount(userLotto)
}
