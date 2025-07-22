package com.currenjin

import com.currenjin.event.OrderEvent
import com.currenjin.event.PaymentEvent
import java.math.BigDecimal

class KotlinPlaygroundApplication

fun main(args: Array<String>) {
    val messageQueue = MessageQueue()
    val publisher = Publisher(messageQueue)
    val orderSubscriber1 = Subscriber(messageQueue = messageQueue, targetEvent = OrderEvent::class)
    val orderSubscriber2 = Subscriber(messageQueue = messageQueue, targetEvent = OrderEvent::class)
    val paymentSubscriber1 = Subscriber(messageQueue = messageQueue, targetEvent = PaymentEvent::class)
    val exceptionPaymentSubscriber = Subscriber(messageQueue = messageQueue, targetEvent = PaymentEvent::class)

    orderSubscriber1.subscribe { event: Message -> println("OrderSubscriber 1 : $event") }
    orderSubscriber2.subscribe { event: Message ->
        Thread.sleep(2000)
        println("OrderSubscriber 2 SLOW : $event")
    }
    paymentSubscriber1.subscribe { event: Message -> println("PaymentSubscriber 3 : $event") }
    exceptionPaymentSubscriber.subscribe { event: Message -> throw RuntimeException("RuntimeException : $event") }

    val firstOrder =
        OrderEvent(
            orderId = "orderId",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        )
    publisher.publish(Message(payload = firstOrder, type = firstOrder.javaClass.simpleName))

    Thread.sleep(1000)
    println(messageQueue.getStatus())

    val secondOrder =
        OrderEvent(
            orderId = "orderId2",
            userId = "userId",
            amount = BigDecimal(100.0),
            eventType = "CREATED",
        )
    publisher.publish(Message(payload = secondOrder, type = secondOrder.javaClass.simpleName))

    Thread.sleep(1000)

    val payment =
        PaymentEvent(
            orderId = "order1",
            amount = BigDecimal(100.0),
            status = "PAID",
        )
    publisher.publish(Message(payload = payment, type = payment.javaClass.simpleName))

    Thread.sleep(5000)
    println(messageQueue.getStatus())
}
