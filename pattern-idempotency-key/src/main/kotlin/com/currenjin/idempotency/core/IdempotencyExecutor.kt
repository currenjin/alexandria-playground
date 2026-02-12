package com.currenjin.idempotency.core

import java.time.Clock
import java.time.Duration
import java.time.Instant

class IdempotencyExecutor<T>(
    private val store: IdempotencyStore<T>,
    private val ttl: Duration = Duration.ofHours(24),
    private val clock: Clock = Clock.systemUTC(),
) {
    val log = mutableListOf<String>()

    fun execute(key: IdempotencyKey, operation: () -> T): IdempotencyResult<T> {
        val existing = store.find(key)

        if (existing != null) {
            return when (existing.status) {
                RecordStatus.COMPLETED -> {
                    log.add("[${key.value}] 중복 요청 감지 - 캐시된 응답 반환")
                    IdempotencyResult.Replayed(existing.response!!)
                }
                RecordStatus.PROCESSING -> {
                    log.add("[${key.value}] 동일 키 처리 중 - 요청 거부")
                    IdempotencyResult.Rejected("동일한 키로 이미 처리 중인 요청이 있습니다")
                }
                RecordStatus.FAILED -> {
                    log.add("[${key.value}] 이전 실패 기록 발견 - 재실행 허용")
                    store.remove(key)
                    executeNew(key, operation)
                }
            }
        }

        return executeNew(key, operation)
    }

    private fun executeNew(key: IdempotencyKey, operation: () -> T): IdempotencyResult<T> {
        val processingRecord = IdempotencyRecord<T>(
            key = key,
            status = RecordStatus.PROCESSING,
            expiresAt = Instant.now(clock).plus(ttl),
        )

        val saved = store.save(processingRecord)
        if (!saved) {
            log.add("[${key.value}] 동시 요청으로 인한 저장 실패 - 요청 거부")
            return IdempotencyResult.Rejected("동시에 동일 키로 요청이 접수되었습니다")
        }

        return try {
            val response = operation()
            val completedRecord = processingRecord.copy(
                status = RecordStatus.COMPLETED,
                response = response,
            )
            store.update(completedRecord)
            log.add("[${key.value}] 작업 실행 완료")
            IdempotencyResult.Executed(response)
        } catch (e: Exception) {
            val failedRecord = processingRecord.copy(
                status = RecordStatus.FAILED,
                errorMessage = e.message,
            )
            store.update(failedRecord)
            log.add("[${key.value}] 작업 실행 실패: ${e.message}")
            throw e
        }
    }
}
