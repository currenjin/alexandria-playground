package com.currenjin

import com.currenjin.policy.concrete.BasicDiscountPolicy
import com.currenjin.policy.concrete.FixedPriceDiscountPolicy
import com.currenjin.policy.concrete.FixedRateDiscountPolicy
import com.currenjin.policy.concrete.VIPDiscountPolicy
import com.currenjin.service.PaymentService

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val fixedPriceDiscountService = PaymentService(FixedPriceDiscountPolicy())
    val fixedRateDiscountService = PaymentService(FixedRateDiscountPolicy())

    println("Fixed Price Discount : ${fixedPriceDiscountService.pay(10_000)}")
    println("Fixed Rate Discount : ${fixedRateDiscountService.pay(10_000)}")

    val vipDiscountService = PaymentService(VIPDiscountPolicy())
    val basicDiscountService = PaymentService(BasicDiscountPolicy())

    println("VIP Discount : ${vipDiscountService.pay(10_000)}")
    println("Basic Discount : ${basicDiscountService.pay(10_000)}")
}
