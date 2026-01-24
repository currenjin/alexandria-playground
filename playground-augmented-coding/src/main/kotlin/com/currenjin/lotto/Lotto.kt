package com.currenjin.lotto

class Lotto(val numbers: List<LottoNumber>) {
    init {
        require(numbers.size >= 6)
    }
}
