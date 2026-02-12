package com.currenjin.idempotency.core

import java.time.Instant

enum class RecordStatus {
    PROCESSING,
    COMPLETED,
    FAILED,
}

data class IdempotencyRecord<T>(
    val key: IdempotencyKey,
    val status: RecordStatus,
    val response: T? = null,
    val errorMessage: String? = null,
    val createdAt: Instant = Instant.now(),
    val expiresAt: Instant,
)
