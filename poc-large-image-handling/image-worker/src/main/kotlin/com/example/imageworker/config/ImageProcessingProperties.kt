package com.example.imageworker.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "image.processing")
data class ImageProcessingProperties(
    val maxFileSizeBytes: Long = 10 * 1024 * 1024,  // 10MB
    val maxPixels: Long = 25_000_000                 // 25 million
)
