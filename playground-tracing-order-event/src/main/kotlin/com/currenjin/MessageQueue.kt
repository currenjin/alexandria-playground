package com.currenjin

import com.currenjin.event.OrderEvent
import java.util.concurrent.ConcurrentLinkedQueue

class MessageQueue {
    private val queue = ConcurrentLinkedQueue<OrderEvent>()

    fun push(event: OrderEvent) {
        queue.offer(event)
    }

    fun poll(): OrderEvent = queue.poll()
}
