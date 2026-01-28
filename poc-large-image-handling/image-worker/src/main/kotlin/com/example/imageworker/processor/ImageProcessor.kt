package com.example.imageworker.processor

import com.example.common.domain.*
import com.example.imageworker.config.ImageProcessingProperties
import com.example.imageworker.config.MinioProperties
import com.example.imageworker.repository.ImageAssetRepository
import com.example.imageworker.repository.ImageVariantRepository
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream
import java.util.*

data class VariantConfig(
    val type: VariantType,
    val width: Int
)

@Component
class ImageProcessor(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties,
    private val processingProperties: ImageProcessingProperties,
    private val assetRepository: ImageAssetRepository,
    private val variantRepository: ImageVariantRepository,
    private val meterRegistry: MeterRegistry
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val variantConfigs = listOf(
        VariantConfig(VariantType.THUMB, 200),
        VariantConfig(VariantType.SMALL, 480),
        VariantConfig(VariantType.MEDIUM, 1080)
    )

    @Transactional
    fun process(assetId: UUID, objectKey: String) {
        val timer = Timer.start(meterRegistry)

        try {
            val asset = assetRepository.findById(assetId)
                .orElseThrow { IllegalStateException("Asset not found: $assetId") }

            if (asset.status != AssetStatus.PENDING && asset.status != AssetStatus.FAILED) {
                log.info("Skipping asset {} with status {}", assetId, asset.status)
                return
            }

            asset.status = AssetStatus.PROCESSING
            assetRepository.save(asset)

            log.info("Processing image: assetId={}, objectKey={}", assetId, objectKey)

            val imageBytes = downloadImage(objectKey)
            validateImage(imageBytes)

            val image = ImmutableImage.loader().fromBytes(imageBytes)
            val originalWidth = image.width
            val originalHeight = image.height

            asset.width = originalWidth
            asset.height = originalHeight
            asset.bytes = imageBytes.size.toLong()

            for (config in variantConfigs) {
                if (originalWidth <= config.width) {
                    log.info("Skipping variant {} for asset {} (original width {} <= target {})",
                        config.type, assetId, originalWidth, config.width)
                    continue
                }

                val existingVariant = variantRepository.findByAssetIdAndType(assetId, config.type)
                if (existingVariant != null) {
                    log.info("Variant {} already exists for asset {}", config.type, assetId)
                    continue
                }

                val resized = image.scaleToWidth(config.width)
                val variantBytes = resized.bytes(WebpWriter.DEFAULT)

                val variantKey = "variants/${assetId}/${config.type.name.lowercase()}.webp"
                uploadVariant(variantKey, variantBytes)

                val variant = ImageVariant(
                    asset = asset,
                    type = config.type,
                    format = ImageFormat.WEBP,
                    objectKey = variantKey,
                    width = resized.width,
                    height = resized.height,
                    bytes = variantBytes.size.toLong()
                )
                variantRepository.save(variant)
                asset.variants.add(variant)

                log.info("Created variant {} for asset {}: {}x{}, {} bytes",
                    config.type, assetId, resized.width, resized.height, variantBytes.size)

                meterRegistry.counter("image.variants.generated", "type", config.type.name).increment()
            }

            asset.status = AssetStatus.READY
            assetRepository.save(asset)

            log.info("Successfully processed image: assetId={}", assetId)

        } catch (e: Exception) {
            log.error("Failed to process image: assetId={}, error={}", assetId, e.message, e)
            meterRegistry.counter("image.process.failures").increment()

            try {
                val asset = assetRepository.findById(assetId).orElse(null)
                if (asset != null) {
                    asset.status = AssetStatus.FAILED
                    asset.errorMessage = e.message?.take(500)
                    assetRepository.save(asset)
                }
            } catch (ex: Exception) {
                log.error("Failed to update asset status to FAILED: {}", ex.message)
            }

            throw e
        } finally {
            timer.stop(meterRegistry.timer("image.process.duration"))
        }
    }

    private fun downloadImage(objectKey: String): ByteArray {
        log.debug("Downloading image from MinIO: {}", objectKey)

        val response = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minioProperties.bucket)
                .`object`(objectKey)
                .build()
        )

        return response.use { it.readBytes() }
    }

    private fun validateImage(imageBytes: ByteArray) {
        if (imageBytes.size > processingProperties.maxFileSizeBytes) {
            throw IllegalArgumentException(
                "Image size ${imageBytes.size} exceeds maximum ${processingProperties.maxFileSizeBytes}"
            )
        }

        val image = try {
            ImmutableImage.loader().fromBytes(imageBytes)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to decode image: ${e.message}")
        }

        val pixels = image.width.toLong() * image.height.toLong()
        if (pixels > processingProperties.maxPixels) {
            throw IllegalArgumentException(
                "Image dimensions ${image.width}x${image.height} ($pixels pixels) exceed maximum ${processingProperties.maxPixels}"
            )
        }
    }

    private fun uploadVariant(variantKey: String, variantBytes: ByteArray) {
        log.debug("Uploading variant to MinIO: {}", variantKey)

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minioProperties.bucket)
                .`object`(variantKey)
                .stream(ByteArrayInputStream(variantBytes), variantBytes.size.toLong(), -1)
                .contentType("image/webp")
                .build()
        )
    }
}
