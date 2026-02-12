package com.currenjin.idempotency.message

data class MessageResult(
    val messageId: String,
    val processed: Boolean,
    val output: String,
)
