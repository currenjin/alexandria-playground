package com.currenjin.saga.core

class SagaEventBus {
    private val handlers = mutableMapOf<String, MutableList<(Any) -> Unit>>()
    val publishedEvents = mutableListOf<Pair<String, Any>>()

    fun subscribe(eventType: String, handler: (Any) -> Unit) {
        handlers.getOrPut(eventType) { mutableListOf() }.add(handler)
    }

    fun publish(eventType: String, event: Any) {
        publishedEvents.add(eventType to event)
        handlers[eventType]?.forEach { handler ->
            handler(event)
        }
    }

    fun clear() {
        handlers.clear()
        publishedEvents.clear()
    }
}
