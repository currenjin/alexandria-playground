package com.example.imageapi.controller

import com.example.imageapi.dto.*
import com.example.imageapi.service.ImageService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping("/presign")
    fun createPresignedUpload(@Valid @RequestBody request: PresignRequest): ResponseEntity<PresignResponse> {
        val response = imageService.createPresignedUpload(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/complete")
    fun completeUpload(@Valid @RequestBody request: CompleteRequest): ResponseEntity<Map<String, String>> {
        val response = imageService.completeUpload(request)
        return ResponseEntity.accepted().body(response)
    }

    @GetMapping("/{assetId}")
    fun getImage(@PathVariable assetId: UUID): ResponseEntity<ImageResponse> {
        val response = imageService.getImage(assetId)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{assetId}")
    fun deleteImage(@PathVariable assetId: UUID): ResponseEntity<Void> {
        imageService.deleteImage(assetId)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("/admin/images")
class AdminImageController(private val imageService: ImageService) {

    @PostMapping("/{assetId}/reprocess")
    fun reprocessImage(@PathVariable assetId: UUID): ResponseEntity<Map<String, String>> {
        val response = imageService.reprocessImage(assetId)
        return ResponseEntity.ok(response)
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to (e.message ?: "Not found")))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("error" to (e.message ?: "Bad request")))
    }
}
