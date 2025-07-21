package com.currenjin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class Subscriber(
    private val messageQueue: MessageQueue,
) {
    private val subscriberId = UUID.randomUUID().toString()

    fun subscribe(callback: (Message) -> Unit) {
        messageQueue.subscribe(subscriberId)

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val event = messageQueue.poll(subscriberId)

                if (event != null) {
                    processWithRetry(callback, event)
                }

                delay(100)
            }
        }
    }

    private suspend fun processWithRetry(
        callback: (Message) -> Unit,
        event: Message,
    ) {
        val maxRetries = 3
        var attempt: Int = 0

        while (attempt < maxRetries) {
            try {
                callback(event)
                messageQueue.ack(subscriberId)
                return
            } catch (e: Exception) {
                attempt++
                println("재시도 $attempt/$maxRetries - ${e.message}")

                if (attempt >= maxRetries) {
                    println("재시도 후 포기: $event")
                    messageQueue.ack(subscriberId)
                    return
                }

                delay(1000)
            }
        }
    }
}
