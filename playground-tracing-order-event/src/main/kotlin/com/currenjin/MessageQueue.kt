package com.currenjin

import com.currenjin.event.OrderEvent
import java.util.Collections

class MessageQueue {
    private val messages = Collections.synchronizedList(mutableListOf<OrderEvent>())
    private val subscriberIds = mutableListOf<String>()

    fun push(event: OrderEvent) {
        messages.add(event)
    }

    fun poll(): OrderEvent? {
        if (messages.isNotEmpty()) {
            return messages.first()
        }

        return null
    }

    fun subscribe(subscriberId: String) {
        subscriberIds.add(subscriberId)
    }
}
