package com.currenjin.outboxinbox.core

class TransactionalOutbox(
    private val outboxStore: OutboxStore,
) {
    val log = mutableListOf<String>()

    fun <T> executeWithOutbox(
        record: OutboxRecord,
        operation: () -> T,
    ): T {
        try {
            val result = operation()
            outboxStore.save(record)
            log.add("[Outbox] 비즈니스 데이터와 이벤트가 원자적으로 저장됨: ${record.eventType}")
            return result
        } catch (e: Exception) {
            log.add("[Outbox] 트랜잭션 실패 - 비즈니스 데이터와 이벤트 모두 롤백: ${e.message}")
            throw e
        }
    }
}
