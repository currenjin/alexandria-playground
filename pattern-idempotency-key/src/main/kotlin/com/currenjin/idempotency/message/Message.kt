package com.currenjin.idempotency.message

data class Message(
    val messageId: String,
    val topic: String,
    val payload: String,
)
