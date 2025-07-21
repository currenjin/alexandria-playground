package com.currenjin

class Publisher(
    private val messageQueue: MessageQueue,
) {
    fun publish(message: Message) {
        messageQueue.push(message)
    }
}
