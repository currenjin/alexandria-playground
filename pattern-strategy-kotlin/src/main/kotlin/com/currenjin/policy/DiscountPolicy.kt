package com.currenjin.policy

interface DiscountPolicy {
    fun discount(price: Int): Int
}
