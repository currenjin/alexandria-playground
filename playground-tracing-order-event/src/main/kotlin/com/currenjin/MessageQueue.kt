package com.currenjin

import kotlin.reflect.KClass

class MessageQueue : AbstractMessageQueue<Message>() {
    private val subscriberIds = mutableListOf<String>()
    private val subscriberProgress = mutableMapOf<String, Int>()

    fun getStatus() = "Messages: ${messages.size}, Progress: $subscriberProgress"

    fun subscribe(subscriberId: String) {
        subscriberIds.add(subscriberId)
        subscriberProgress[subscriberId] = 0
    }

    fun <T : Any> poll(
        subscriberId: String,
        targetEvent: KClass<T>,
    ): Message? {
        val index = subscriberProgress[subscriberId] ?: 0

        if (index < messages.size) {
            val message = messages[index]
            if (message.type == targetEvent.simpleName) {
                return message
            } else {
                ack(subscriberId)
            }
        }
        return null
    }

    fun ack(subscriberId: String) {
        val currentIndex = subscriberProgress[subscriberId] ?: 0
        subscriberProgress[subscriberId] = currentIndex + 1

        cleanupMessages()
    }

    override fun cleanupMessages() {
        val minimumIndex = subscriberProgress.minBy { it.value }.value

        if (minimumIndex > 0 && minimumIndex <= messages.size) {
            repeat(minimumIndex) {
                messages.removeFirst()
            }

            subscriberProgress.keys.forEach { subscriberId ->
                val currentIndex = subscriberProgress[subscriberId] ?: 0
                subscriberProgress[subscriberId] = currentIndex - minimumIndex
            }
        }
    }
}
