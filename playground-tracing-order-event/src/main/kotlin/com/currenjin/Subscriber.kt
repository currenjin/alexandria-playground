package com.currenjin

import com.currenjin.event.OrderEvent

class Subscriber(
    private val messageQueue: MessageQueue,
) {
    fun subscribe(callback: (OrderEvent) -> Unit) {
        messageQueue.subscribe(callback)
    }
}
