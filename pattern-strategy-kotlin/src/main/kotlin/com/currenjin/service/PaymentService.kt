package com.currenjin.service

import com.currenjin.policy.DiscountPolicy

class PaymentService(
    private val discountPolicy: DiscountPolicy,
) {
    fun pay(price: Int): Int = discountPolicy.discount(price)
}
