package com.currenjin

import com.currenjin.membership.MembershipLevel
import com.currenjin.service.PaymentService

class KotlinPlaygroundApplication

fun main() {
    val paymentService = PaymentService()

    println("VIP 회원 결제 금액: ${paymentService.payment(MembershipLevel.VIP, 10000)}")

    println("BASIC 회원 결제 금액: ${paymentService.payment(MembershipLevel.BASIC, 10000)}")
}
