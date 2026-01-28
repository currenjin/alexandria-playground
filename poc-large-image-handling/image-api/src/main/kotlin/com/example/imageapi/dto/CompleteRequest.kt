package com.example.imageapi.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class CompleteRequest(
    @field:NotNull(message = "assetId is required")
    val assetId: UUID,
    val idempotencyKey: String? = null,
    @field:NotBlank(message = "objectKey is required")
    val objectKey: String
)
