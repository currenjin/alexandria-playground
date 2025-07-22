package com.currenjin

import java.util.Collections

abstract class AbstractMessageQueue<T> {
    protected val messages: MutableList<Message> = Collections.synchronizedList(mutableListOf<Message>())

    fun push(message: Message) {
        messages.add(message)
    }

    abstract fun cleanupMessages()
}
