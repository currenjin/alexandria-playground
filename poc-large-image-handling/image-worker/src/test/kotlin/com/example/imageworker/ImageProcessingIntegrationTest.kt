package com.example.imageworker

import com.example.common.domain.AssetStatus
import com.example.common.domain.ImageAsset
import com.example.common.domain.VariantType
import com.example.common.event.ImageUploadedEvent
import com.example.imageworker.repository.ImageAssetRepository
import com.example.imageworker.repository.ImageVariantRepository
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayInputStream
import java.time.Duration
import java.util.*
import javax.imageio.ImageIO
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Testcontainers
class ImageProcessingIntegrationTest {

    companion object {
        private const val BUCKET_NAME = "images"

        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("imagedb")
            .withUsername("imageuser")
            .withPassword("imagepass")

        @Container
        @JvmStatic
        val minio = MinIOContainer("minio/minio:latest")
            .withUserName("minioadmin")
            .withPassword("minioadmin")

        @Container
        @JvmStatic
        val rabbitmq = RabbitMQContainer("rabbitmq:3-management-alpine")
            .withUser("guest", "guest")

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }

            registry.add("minio.endpoint") { minio.s3URL }
            registry.add("minio.access-key") { "minioadmin" }
            registry.add("minio.secret-key") { "minioadmin" }
            registry.add("minio.bucket") { BUCKET_NAME }

            registry.add("spring.rabbitmq.host") { rabbitmq.host }
            registry.add("spring.rabbitmq.port") { rabbitmq.amqpPort }
            registry.add("spring.rabbitmq.username") { "guest" }
            registry.add("spring.rabbitmq.password") { "guest" }
        }
    }

    @Autowired
    private lateinit var assetRepository: ImageAssetRepository

    @Autowired
    private lateinit var variantRepository: ImageVariantRepository

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    private lateinit var minioClient: MinioClient

    @BeforeEach
    fun setup() {
        variantRepository.deleteAll()
        assetRepository.deleteAll()
        ensureBucketExists()
    }

    private fun ensureBucketExists() {
        val exists = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(BUCKET_NAME).build()
        )
        if (!exists) {
            minioClient.makeBucket(
                MakeBucketArgs.builder().bucket(BUCKET_NAME).build()
            )
        }
    }

    @Test
    fun `should process image and create variants when upload complete event is received`() {
        // Given: Create asset and upload test image
        val assetId = UUID.randomUUID()
        val objectKey = "original/${assetId}/test.jpg"

        val asset = ImageAsset(
            id = assetId,
            status = AssetStatus.PENDING,
            originalKey = objectKey,
            filename = "test.jpg",
            mime = "image/jpeg"
        )
        assetRepository.save(asset)

        // Create and upload a test image (1200x800 pixels)
        val testImageBytes = createTestImage(1200, 800)
        uploadToMinio(objectKey, testImageBytes, "image/jpeg")

        // When: Send ImageUploadedEvent
        val event = ImageUploadedEvent(
            assetId = assetId,
            objectKey = objectKey
        )
        rabbitTemplate.convertAndSend("image.exchange", "image.uploaded", event)

        // Then: Wait for processing and verify
        await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofMillis(500))
            .until {
                val updatedAsset = assetRepository.findById(assetId).orElse(null)
                updatedAsset?.status == AssetStatus.READY
            }

        val updatedAsset = assetRepository.findById(assetId).get()
        assertEquals(AssetStatus.READY, updatedAsset.status)
        assertEquals(1200, updatedAsset.width)
        assertEquals(800, updatedAsset.height)

        val variants = variantRepository.findAll().filter { it.asset.id == assetId }
        assertEquals(3, variants.size)

        val thumb = variants.find { it.type == VariantType.THUMB }
        assertNotNull(thumb)
        assertEquals(200, thumb.width)

        val small = variants.find { it.type == VariantType.SMALL }
        assertNotNull(small)
        assertEquals(480, small.width)

        val medium = variants.find { it.type == VariantType.MEDIUM }
        assertNotNull(medium)
        assertEquals(1080, medium.width)
    }

    @Test
    fun `should mark asset as FAILED when processing corrupted image`() {
        // Given: Create asset and upload corrupted image
        val assetId = UUID.randomUUID()
        val objectKey = "original/${assetId}/corrupted.jpg"

        val asset = ImageAsset(
            id = assetId,
            status = AssetStatus.PENDING,
            originalKey = objectKey,
            filename = "corrupted.jpg",
            mime = "image/jpeg"
        )
        assetRepository.save(asset)

        // Upload corrupted/invalid image data
        val corruptedData = "not an image".toByteArray()
        uploadToMinio(objectKey, corruptedData, "image/jpeg")

        // When: Send ImageUploadedEvent
        val event = ImageUploadedEvent(
            assetId = assetId,
            objectKey = objectKey
        )
        rabbitTemplate.convertAndSend("image.exchange", "image.uploaded", event)

        // Then: Wait for processing failure and verify
        await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofMillis(500))
            .until {
                val updatedAsset = assetRepository.findById(assetId).orElse(null)
                updatedAsset?.status == AssetStatus.FAILED
            }

        val updatedAsset = assetRepository.findById(assetId).get()
        assertEquals(AssetStatus.FAILED, updatedAsset.status)
        assertTrue(updatedAsset.errorMessage?.isNotEmpty() == true)

        val variants = variantRepository.findAll().filter { it.asset.id == assetId }
        assertTrue(variants.isEmpty())
    }

    private fun createTestImage(width: Int, height: Int): ByteArray {
        val image = java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB)
        val g2d = image.createGraphics()
        g2d.color = java.awt.Color.BLUE
        g2d.fillRect(0, 0, width, height)
        g2d.dispose()

        val outputStream = java.io.ByteArrayOutputStream()
        ImageIO.write(image, "jpg", outputStream)
        return outputStream.toByteArray()
    }

    private fun uploadToMinio(objectKey: String, data: ByteArray, contentType: String) {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .`object`(objectKey)
                .stream(ByteArrayInputStream(data), data.size.toLong(), -1)
                .contentType(contentType)
                .build()
        )
    }
}
