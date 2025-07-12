package com.currenjin

import com.currenjin.event.OrderEvent
import java.math.BigDecimal

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val messageQueue = MessageQueue()
    val publisher = Publisher(messageQueue)
    val subscriber = Subscriber(messageQueue)

    publisher.publish(
        OrderEvent(
            orderId = "orderId",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        ),
    )

    subscriber.subscribe { event: Message -> println("order event : $event") }
}
