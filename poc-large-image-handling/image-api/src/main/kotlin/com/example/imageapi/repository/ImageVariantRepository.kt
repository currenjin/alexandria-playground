package com.example.imageapi.repository

import com.example.common.domain.ImageVariant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageVariantRepository : JpaRepository<ImageVariant, UUID> {
    fun findByAssetId(assetId: UUID): List<ImageVariant>
}
