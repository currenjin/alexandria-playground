package com.currenjin.policy.concrete

import com.currenjin.policy.DiscountPolicy

class FixedPriceDiscountPolicy : DiscountPolicy {
    override fun discount(price: Int): Int = price - 1_000
}
