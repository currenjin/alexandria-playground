package com.currenjin

import com.currenjin.event.OrderEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Subscriber(
    private val messageQueue: MessageQueue,
) {
    fun subscribe(callback: (OrderEvent) -> Unit) {
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
