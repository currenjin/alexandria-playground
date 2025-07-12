package com.currenjin.event

import com.currenjin.Message
import java.math.BigDecimal

data class OrderEvent(
    val orderId: String,
    val userId: String,
    val amount: BigDecimal,
    val eventType: String,
) : Message()
