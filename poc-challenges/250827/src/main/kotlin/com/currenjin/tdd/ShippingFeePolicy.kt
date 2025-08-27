package com.currenjin.tdd

class ShippingFeePolicy(
    private val threshold: Money = Money.of(50_000),
    private val fee: Money = Money.of(3_000),
) {
    fun calculate(subtotal: Money) = if (subtotal >= threshold) Money.zero() else fee
}
