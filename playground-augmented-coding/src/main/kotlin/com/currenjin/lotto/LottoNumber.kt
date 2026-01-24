package com.currenjin.lotto

data class LottoNumber(val value: Int) {
    init {
        require(value in 1..45)
    }
}
