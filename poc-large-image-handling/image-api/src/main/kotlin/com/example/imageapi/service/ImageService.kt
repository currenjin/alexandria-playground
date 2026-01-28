package com.example.imageapi.service

import com.example.common.domain.AssetStatus
import com.example.common.domain.ImageAsset
import com.example.common.event.ImageUploadedEvent
import com.example.imageapi.config.MinioProperties
import com.example.imageapi.config.RabbitConfig
import com.example.imageapi.dto.*
import com.example.imageapi.repository.ImageAssetRepository
import com.example.imageapi.repository.ImageVariantRepository
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ImageService(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
    private val assetRepository: ImageAssetRepository,
    private val variantRepository: ImageVariantRepository,
    private val rabbitTemplate: RabbitTemplate
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createPresignedUpload(request: PresignRequest): PresignResponse {
        val assetId = UUID.randomUUID()
        val objectKey = "original/${assetId}/${request.filename}"

        val asset = ImageAsset(
            id = assetId,
            status = AssetStatus.PENDING,
            originalKey = objectKey,
            ownerType = request.ownerType,
            ownerId = request.ownerId,
            filename = request.filename,
            mime = request.contentType
        )
        assetRepository.save(asset)

        val expiryMinutes = minioProperties.presignExpiryMinutes
        val uploadUrl = minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(minioProperties.bucket)
                .`object`(objectKey)
                .expiry(expiryMinutes.toInt(), TimeUnit.MINUTES)
                .build()
        )

        val expiresAt = Instant.now().plusSeconds(expiryMinutes * 60)

        log.info("Created presigned URL for assetId={}, objectKey={}", assetId, objectKey)

        return PresignResponse(
            assetId = assetId,
            uploadUrl = uploadUrl,
            objectKey = objectKey,
            expiresAt = expiresAt
        )
    }

    @Transactional
    fun completeUpload(request: CompleteRequest): Map<String, String> {
        val asset = assetRepository.findByIdAndDeletedAtIsNull(request.assetId)
            ?: throw NoSuchElementException("Asset not found: ${request.assetId}")

        if (asset.status != AssetStatus.PENDING && asset.status != AssetStatus.FAILED) {
            log.info("Asset {} already processed, status={}", request.assetId, asset.status)
            return mapOf("status" to "ALREADY_PROCESSED")
        }

        val event = ImageUploadedEvent(
            assetId = request.assetId,
            objectKey = request.objectKey
        )

        rabbitTemplate.convertAndSend(
            RabbitConfig.IMAGE_EXCHANGE,
            RabbitConfig.IMAGE_UPLOADED_ROUTING_KEY,
            event
        )

        log.info("Published ImageUploadedEvent for assetId={}", request.assetId)

        return mapOf("status" to "ACCEPTED")
    }

    @Transactional(readOnly = true)
    fun getImage(assetId: UUID): ImageResponse {
        val asset = assetRepository.findByIdAndDeletedAtIsNull(assetId)
            ?: throw NoSuchElementException("Asset not found: $assetId")

        val variants = variantRepository.findByAssetId(assetId)

        val originalUrl = if (asset.status == AssetStatus.READY || asset.status == AssetStatus.PROCESSING) {
            "${minioProperties.endpoint}/${minioProperties.bucket}/${asset.originalKey}"
        } else null

        val variantDtos = variants.map { v ->
            VariantDto(
                type = v.type.name,
                url = "${minioProperties.endpoint}/${minioProperties.bucket}/${v.objectKey}",
                width = v.width,
                height = v.height,
                bytes = v.bytes,
                format = v.format.name
            )
        }

        return ImageResponse(
            assetId = asset.id,
            status = asset.status,
            originalUrl = originalUrl,
            variants = variantDtos
        )
    }

    @Transactional
    fun deleteImage(assetId: UUID) {
        val asset = assetRepository.findByIdAndDeletedAtIsNull(assetId)
            ?: throw NoSuchElementException("Asset not found: $assetId")

        asset.status = AssetStatus.DELETED
        asset.deletedAt = Instant.now()
        assetRepository.save(asset)

        log.info("Soft deleted asset {}", assetId)
    }

    @Transactional
    fun reprocessImage(assetId: UUID): Map<String, String> {
        val asset = assetRepository.findById(assetId)
            .orElseThrow { NoSuchElementException("Asset not found: $assetId") }

        if (asset.status == AssetStatus.DELETED) {
            throw IllegalStateException("Cannot reprocess deleted asset: $assetId")
        }

        asset.status = AssetStatus.PENDING
        asset.errorMessage = null
        assetRepository.save(asset)

        val event = ImageUploadedEvent(
            assetId = assetId,
            objectKey = asset.originalKey
        )

        rabbitTemplate.convertAndSend(
            RabbitConfig.IMAGE_EXCHANGE,
            RabbitConfig.IMAGE_UPLOADED_ROUTING_KEY,
            event
        )

        log.info("Reprocess triggered for assetId={}", assetId)

        return mapOf("status" to "REPROCESS_TRIGGERED")
    }
}
