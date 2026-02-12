package com.currenjin.outboxinbox.scenario.order

interface PaymentStore {
    fun save(payment: Payment)
    fun findByOrderId(orderId: String): Payment?
    fun findAll(): List<Payment>
}
