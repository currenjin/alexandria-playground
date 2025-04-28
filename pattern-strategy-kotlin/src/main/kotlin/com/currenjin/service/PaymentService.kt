package com.currenjin.service

import com.currenjin.membership.MembershipLevel
import com.currenjin.policy.DiscountPolicy
import com.currenjin.policy.concrete.BasicDiscountPolicy
import com.currenjin.policy.concrete.VIPDiscountPolicy

class PaymentService {
    private val discountPolicyMap: Map<MembershipLevel, DiscountPolicy> =
        mapOf(
            MembershipLevel.VIP to VIPDiscountPolicy(),
            MembershipLevel.BASIC to BasicDiscountPolicy(),
        )

    fun payment(
        membershipLevel: MembershipLevel,
        price: Int,
    ): Int {
        val policy = discountPolicyMap[membershipLevel] ?: throw IllegalArgumentException("Unknown membership level: $membershipLevel")
        return policy.discount(price)
    }
}
