package com.currenjin

import com.currenjin.event.OrderEvent
import java.util.concurrent.ConcurrentLinkedQueue

class MessageQueue {
    private val queue = ConcurrentLinkedQueue<OrderEvent>()
    private val subscriberIds = mutableListOf<String>()

    fun push(event: OrderEvent) {
        queue.offer(event)
    }

    fun poll(): OrderEvent = queue.poll()

    fun subscribe(subscriberId: String) {
        subscriberIds.add(subscriberId)
    }
}
