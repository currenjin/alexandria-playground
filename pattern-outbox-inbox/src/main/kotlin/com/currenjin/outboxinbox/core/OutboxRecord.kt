package com.currenjin.outboxinbox.core

import java.time.Instant
import java.util.UUID

enum class OutboxStatus {
    PENDING,
    SENT,
    FAILED,
}

data class OutboxRecord(
    val id: String = UUID.randomUUID().toString(),
    val aggregateType: String,
    val aggregateId: String,
    val eventType: String,
    val payload: String,
    val status: OutboxStatus = OutboxStatus.PENDING,
    val createdAt: Instant = Instant.now(),
    val sentAt: Instant? = null,
    val retryCount: Int = 0,
)
