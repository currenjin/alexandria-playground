package com.example.imageapi.dto

import java.time.Instant
import java.util.*

data class PresignResponse(
    val assetId: UUID,
    val uploadUrl: String,
    val objectKey: String,
    val expiresAt: Instant
)
