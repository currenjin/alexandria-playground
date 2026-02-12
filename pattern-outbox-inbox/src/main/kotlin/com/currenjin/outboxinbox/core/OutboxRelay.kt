package com.currenjin.outboxinbox.core

import java.time.Instant

class OutboxRelay(
    private val outboxStore: OutboxStore,
    private val messageBroker: MessageBroker,
) {
    val log = mutableListOf<String>()

    fun pollAndPublish(): Int {
        val pending = outboxStore.findPending()
        var published = 0

        for (record in pending) {
            try {
                val message = Message(
                    id = record.id,
                    eventType = record.eventType,
                    payload = record.payload,
                    aggregateType = record.aggregateType,
                    aggregateId = record.aggregateId,
                )
                messageBroker.publish(record.eventType, message)
                outboxStore.markSent(record.id, Instant.now())
                log.add("[Relay] 이벤트 발행 완료: ${record.eventType} (${record.id})")
                published++
            } catch (e: Exception) {
                outboxStore.markFailed(record.id)
                log.add("[Relay] 이벤트 발행 실패: ${record.eventType} (${record.id}) - ${e.message}")
            }
        }

        return published
    }
}
