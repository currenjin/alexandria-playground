package com.currenjin.tdd

class ShippingFeePolicy {
    fun calculate(order: Order): Money {
        if (order.totalAmount.totalAmount > 50000) {
            return Money.of(0)
        }

        return Money.of(3000)
    }
}
