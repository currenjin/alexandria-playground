package com.currenjin

import com.currenjin.event.OrderEvent
import java.util.Collections

class MessageQueue {
    private val messages = Collections.synchronizedList(mutableListOf<OrderEvent>())
    private val subscriberIds = mutableListOf<String>()
    private val subscriberProgress = mutableMapOf<String, Int>()

    fun push(event: OrderEvent) {
        messages.add(event)
    }

    fun subscribe(subscriberId: String) {
        subscriberIds.add(subscriberId)
        subscriberProgress[subscriberId] = 0
    }

    fun poll(subscriberId: String): OrderEvent? {
        val index = subscriberProgress[subscriberId] ?: 0

        if (index < messages.size) {
            return messages[index]
        }

        return null
    }

    fun ack(subscriberId: String) {
        val currentIndex = subscriberProgress[subscriberId] ?: 0
        subscriberProgress[subscriberId] = currentIndex + 1
    }
}
