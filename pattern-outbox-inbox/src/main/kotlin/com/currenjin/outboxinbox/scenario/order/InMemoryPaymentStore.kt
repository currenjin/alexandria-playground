package com.currenjin.outboxinbox.scenario.order

class InMemoryPaymentStore : PaymentStore {
    private val payments = mutableMapOf<String, Payment>()

    override fun save(payment: Payment) {
        payments[payment.orderId] = payment
    }

    override fun findByOrderId(orderId: String): Payment? = payments[orderId]

    override fun findAll(): List<Payment> = payments.values.toList()
}
