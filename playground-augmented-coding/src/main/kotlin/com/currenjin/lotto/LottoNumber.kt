package com.currenjin.lotto

class LottoNumber(val value: Int) {
    init {
        require(value in 1..45)
    }
}
