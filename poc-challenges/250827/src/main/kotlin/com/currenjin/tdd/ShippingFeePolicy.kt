package com.currenjin.tdd

class ShippingFeePolicy(
    private val threshold: Money = Money.of(50_000),
    private val fee: Money = Money.of(3_000),
) {
    fun calculate(order: Order): Money =
        if (order.totalAmount.totalAmount >= threshold.totalAmount) {
            Money.of(0)
        } else {
            fee
        }
}
