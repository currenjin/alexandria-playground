package com.currenjin.tdd

class Dollar(
    var amount: Int
) {
    fun times(multiplier: Int): Dollar {
        return Dollar(amount * multiplier)
    }
}
