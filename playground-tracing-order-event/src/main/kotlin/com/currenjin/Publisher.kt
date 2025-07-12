package com.currenjin

import com.currenjin.event.OrderEvent

class Publisher(
    private val messageQueue: MessageQueue,
) {
    fun publish(event: OrderEvent) {
        messageQueue.push(event)
    }
}
