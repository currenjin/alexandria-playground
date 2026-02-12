package com.currenjin.outboxinbox.core

data class Message(
    val id: String,
    val eventType: String,
    val payload: String,
    val aggregateType: String,
    val aggregateId: String,
)

interface MessageBroker {
    fun publish(topic: String, message: Message)
    fun subscribe(topic: String, handler: (Message) -> Unit)
}
