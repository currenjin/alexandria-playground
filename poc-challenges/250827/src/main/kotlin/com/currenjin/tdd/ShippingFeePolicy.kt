package com.currenjin.tdd

class ShippingFeePolicy {
    fun calculate(order: Order) = Money.of(3000)
}
