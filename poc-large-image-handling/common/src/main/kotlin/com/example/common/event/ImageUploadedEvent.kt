package com.example.common.event

import java.time.Instant
import java.util.*

data class ImageUploadedEvent(
    val eventId: UUID = UUID.randomUUID(),
    val assetId: UUID,
    val objectKey: String,
    val occurredAt: Instant = Instant.now()
)
