package com.currenjin.policy.concrete

import com.currenjin.policy.DiscountPolicy

class FixedRateDiscountPolicy : DiscountPolicy {
    override fun discount(price: Int): Int = (price * 0.9).toInt()
}
