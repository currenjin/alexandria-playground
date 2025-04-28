package com.currenjin

import com.currenjin.policy.concrete.FixedPriceDiscountPolicy
import com.currenjin.policy.concrete.FixedRateDiscountPolicy
import com.currenjin.service.PaymentService

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val fixedPriceDiscountService = PaymentService(FixedPriceDiscountPolicy())
    val fixedRateDiscountService = PaymentService(FixedRateDiscountPolicy())

    println("Fixed Price Discount : ${fixedPriceDiscountService.pay(10_000)}")
    println("Fixed Rate Discount : ${fixedRateDiscountService.pay(10_000)}")
}
