package com.currenjin

import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val traceId: String = UUID.randomUUID().toString(),
    val payload: Any,
    val type: String,
    val timestamp: Long = System.currentTimeMillis(),
)
