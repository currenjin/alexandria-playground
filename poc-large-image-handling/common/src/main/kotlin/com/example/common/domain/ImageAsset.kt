package com.example.common.domain

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "image_asset")
class ImageAsset(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: AssetStatus = AssetStatus.PENDING,

    @Column(name = "original_key", nullable = false)
    val originalKey: String,

    @Column
    var checksum: String? = null,

    @Column
    var mime: String? = null,

    @Column
    var width: Int? = null,

    @Column
    var height: Int? = null,

    @Column
    var bytes: Long? = null,

    @Column(name = "owner_type")
    val ownerType: String? = null,

    @Column(name = "owner_id")
    val ownerId: String? = null,

    @Column(name = "filename")
    val filename: String? = null,

    @Column(name = "error_message")
    var errorMessage: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null,

    @OneToMany(mappedBy = "asset", cascade = [CascadeType.ALL], orphanRemoval = true)
    val variants: MutableList<ImageVariant> = mutableListOf()
)

enum class AssetStatus {
    PENDING,
    PROCESSING,
    READY,
    FAILED,
    DELETED
}
