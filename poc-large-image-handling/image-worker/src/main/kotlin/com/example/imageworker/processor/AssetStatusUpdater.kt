package com.example.imageworker.processor

import com.example.common.domain.AssetStatus
import com.example.imageworker.repository.ImageAssetRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class AssetStatusUpdater(
    private val assetRepository: ImageAssetRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun markAsFailed(assetId: UUID, errorMessage: String?) {
        try {
            val asset = assetRepository.findById(assetId).orElse(null)
            if (asset != null) {
                asset.status = AssetStatus.FAILED
                asset.errorMessage = errorMessage?.take(250)
                assetRepository.save(asset)
                log.info("Marked asset {} as FAILED", assetId)
            }
        } catch (ex: Exception) {
            log.error("Failed to update asset status to FAILED: {}", ex.message)
        }
    }
}
