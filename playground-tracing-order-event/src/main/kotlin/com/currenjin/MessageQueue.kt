package com.currenjin

import com.currenjin.event.OrderEvent
import java.util.concurrent.ConcurrentLinkedQueue

class MessageQueue {
    private val queue = ConcurrentLinkedQueue<OrderEvent>()
    private val subscribers = mutableListOf<(OrderEvent) -> Unit>()

    fun push(event: OrderEvent) {
        queue.offer(event)

        subscribers.forEach { callback -> callback(event) }
    }

    fun subscribe(callback: (OrderEvent) -> Unit) {
        subscribers.add(callback)
    }
}
