package com.currenjin.lotto

class WinningLotto(val lotto: Lotto, val bonus: LottoNumber) {
    init {
        require(!lotto.contains(bonus))
    }
}
