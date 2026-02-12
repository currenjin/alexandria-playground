package com.currenjin.outboxinbox.core

import java.time.Instant

enum class InboxStatus {
    PROCESSING,
    COMPLETED,
    FAILED,
}

data class InboxRecord(
    val messageId: String,
    val eventType: String,
    val status: InboxStatus,
    val processedAt: Instant? = null,
    val errorMessage: String? = null,
)
