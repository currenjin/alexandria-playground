package com.example.common.domain

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "image_variant")
class ImageVariant(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    val asset: ImageAsset,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: VariantType,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val format: ImageFormat = ImageFormat.WEBP,

    @Column(name = "object_key", nullable = false)
    val objectKey: String,

    @Column(nullable = false)
    val width: Int,

    @Column(nullable = false)
    val height: Int,

    @Column(nullable = false)
    val bytes: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
)

enum class VariantType {
    THUMB,  // 200w
    SMALL,  // 480w
    MEDIUM  // 1080w
}

enum class ImageFormat {
    WEBP
}
