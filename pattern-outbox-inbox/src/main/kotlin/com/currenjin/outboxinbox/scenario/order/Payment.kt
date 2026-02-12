package com.currenjin.outboxinbox.scenario.order

data class Payment(
    val id: String,
    val orderId: String,
    val amount: Long,
)
