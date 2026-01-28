package com.example.imageapi.dto

import jakarta.validation.constraints.NotBlank

data class PresignRequest(
    val ownerType: String? = null,
    val ownerId: String? = null,
    @field:NotBlank(message = "filename is required")
    val filename: String,
    @field:NotBlank(message = "contentType is required")
    val contentType: String
)
