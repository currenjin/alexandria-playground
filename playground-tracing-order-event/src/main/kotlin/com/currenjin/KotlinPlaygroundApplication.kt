package com.currenjin

import java.math.BigDecimal

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val messageQueue = MessageQueue()
    val producer = Producer(messageQueue)
    val consumer = Consumer(messageQueue)

    producer.publish(
        OrderEvent(
            orderId = "orderId",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        ),
    )

    consumer.subscribe { event: Message -> println("order event : $event") }
}

data class OrderEvent(
    val orderId: String,
    val userId: String,
    val amount: BigDecimal,
    val eventType: String,
) : Message()
