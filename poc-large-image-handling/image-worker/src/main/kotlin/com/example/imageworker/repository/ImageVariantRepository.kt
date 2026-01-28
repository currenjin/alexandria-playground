package com.example.imageworker.repository

import com.example.common.domain.ImageVariant
import com.example.common.domain.VariantType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageVariantRepository : JpaRepository<ImageVariant, UUID> {
    fun findByAssetIdAndType(assetId: UUID, type: VariantType): ImageVariant?
}
