package com.currenjin.policy.concrete

import com.currenjin.policy.DiscountPolicy

class BasicDiscountPolicy : DiscountPolicy {
    override fun discount(price: Int): Int = (price * 0.9).toInt()
}
