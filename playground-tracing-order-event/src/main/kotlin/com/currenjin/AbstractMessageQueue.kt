package com.currenjin

import java.util.Collections

abstract class AbstractMessageQueue {
    protected val messages: MutableList<Message> = Collections.synchronizedList(mutableListOf<Message>())

    fun push(message: Message) {
        messages.add(message)
    }

    fun messageSize(): Int = messages.size

    abstract fun cleanupMessages()
}
