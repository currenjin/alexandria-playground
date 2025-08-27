package com.currenjin.tdd

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShippingFeePolicyTest {
    private val policy = ShippingFeePolicy()

    @Test fun order_under_threshold_pays_shipping_fee() {
        val o = Order().apply { add(Money.of(30_000), Qty(1)) }
        assertEquals(Money.of(3_000), policy.calculate(o.subtotal()))
    }

    @Test fun order_at_threshold_is_free_shipping() {
        val o = Order().apply { add(Money.of(50_000), Qty(1)) }
        assertEquals(Money.zero(), policy.calculate(o.subtotal()))
    }

    @Test fun order_above_threshold_is_free_shipping() {
        val o = Order().apply { add(Money.of(60_000), Qty(1)) }
        assertEquals(Money.zero(), policy.calculate(o.subtotal()))
    }

    @Test fun shipping_fee_is_based_on_order_subtotal() {
        val o =
            Order().apply {
                add(Money.of(10_000), Qty(2)) // 20,000
                add(Money.of(15_000), Qty(1)) // 15,000 → subtotal 35,000
            }
        assertEquals(Money.of(3_000), policy.calculate(o.subtotal()))
    }
}
