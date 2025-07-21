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
    val exceptionSubscriber = Subscriber(messageQueue)

    subscriber1.subscribe { event: Message -> println("Number 1 : $event") }
    subscriber2.subscribe { event: Message ->
        Thread.sleep(2000)
        println("Number 2 SLOW : $event")
    }
    subscriber3.subscribe { event: Message -> println("Number 3 : $event") }
    exceptionSubscriber.subscribe { event: Message -> throw RuntimeException("RuntimeException : $event") }

    val firstEvent =
        OrderEvent(
            orderId = "orderId",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        )
    publisher.publish(Message(payload = firstEvent, type = firstEvent.javaClass.simpleName))

    Thread.sleep(1000)
    println(messageQueue.getStatus())

    val secondEvent =
        OrderEvent(
            orderId = "orderId2",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        )
    publisher.publish(Message(payload = secondEvent, type = secondEvent.javaClass.simpleName))

    Thread.sleep(4000)
    println(messageQueue.getStatus())
}
