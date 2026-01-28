package com.example.imageworker.consumer

import com.example.common.event.ImageUploadedEvent
import com.example.imageworker.config.RabbitConfig
import com.example.imageworker.processor.ImageProcessor
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ImageEventConsumer(
    private val imageProcessor: ImageProcessor
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [RabbitConfig.IMAGE_UPLOADED_QUEUE])
    fun handleImageUploaded(event: ImageUploadedEvent) {
        log.info("Received ImageUploadedEvent: eventId={}, assetId={}, objectKey={}",
            event.eventId, event.assetId, event.objectKey)

        imageProcessor.process(event.assetId, event.objectKey)

        log.info("Completed processing ImageUploadedEvent: eventId={}", event.eventId)
    }
}
