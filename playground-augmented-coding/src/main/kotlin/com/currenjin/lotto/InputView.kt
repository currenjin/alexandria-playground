package com.currenjin.lotto

object InputView {
    fun readPurchaseAmount(): Int {
        println("구입금액을 입력해 주세요.")
        return readLine()!!.toInt()
    }
}
