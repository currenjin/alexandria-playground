package com.currenjin

import com.currenjin.event.OrderEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class Subscriber(
    private val messageQueue: MessageQueue,
) {
    private val subscriberId = UUID.randomUUID().toString()

    fun subscribe(callback: (OrderEvent) -> Unit) {
        messageQueue.subscribe(subscriberId)

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val event = messageQueue.poll()
                if (event != null) {
                    callback(event)
                }
                delay(100)
            }
        }
    }
}
