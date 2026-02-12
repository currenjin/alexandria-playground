package com.currenjin.outboxinbox.core

class InMemoryMessageBroker : MessageBroker {
    private val subscribers = mutableMapOf<String, MutableList<(Message) -> Unit>>()
    val publishedMessages = mutableListOf<Pair<String, Message>>()

    override fun publish(topic: String, message: Message) {
        publishedMessages.add(topic to message)
        subscribers[topic]?.forEach { handler ->
            handler(message)
        }
    }

    override fun subscribe(topic: String, handler: (Message) -> Unit) {
        subscribers.getOrPut(topic) { mutableListOf() }.add(handler)
    }
}
