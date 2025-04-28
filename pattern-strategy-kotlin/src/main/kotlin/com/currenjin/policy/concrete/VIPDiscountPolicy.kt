package com.currenjin.policy.concrete

import com.currenjin.policy.DiscountPolicy

class VIPDiscountPolicy : DiscountPolicy {
    override fun discount(price: Int): Int = (price * 0.5).toInt()
}
