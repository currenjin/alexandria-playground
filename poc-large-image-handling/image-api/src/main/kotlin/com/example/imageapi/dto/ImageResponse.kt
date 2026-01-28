package com.example.imageapi.dto

import com.example.common.domain.AssetStatus
import java.util.*

data class ImageResponse(
    val assetId: UUID,
    val status: AssetStatus,
    val originalUrl: String?,
    val variants: List<VariantDto>
)

data class VariantDto(
    val type: String,
    val url: String,
    val width: Int,
    val height: Int,
    val bytes: Long,
    val format: String
)
