package com.currenjin

import com.currenjin.event.OrderEvent
import java.math.BigDecimal

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val messageQueue = MessageQueue()
    val publisher = Publisher(messageQueue)
    val subscriber1 = Subscriber(messageQueue)
    val subscriber2 = Subscriber(messageQueue)
    val subscriber3 = Subscriber(messageQueue)

    subscriber1.subscribe { event: OrderEvent -> println("Number 1 : $event") }
    subscriber2.subscribe { event: OrderEvent ->
        Thread.sleep(2000)
        println("Number 2 SLOW : $event")
    }
    subscriber3.subscribe { event: OrderEvent -> println("Number 3 : $event") }

    publisher.publish(
        OrderEvent(
            orderId = "orderId",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        ),
    )

    Thread.sleep(1000)
    println(messageQueue.getStatus())

    publisher.publish(
        OrderEvent(
            orderId = "orderId2",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        ),
    )

    Thread.sleep(4000)
    println(messageQueue.getStatus())
}
