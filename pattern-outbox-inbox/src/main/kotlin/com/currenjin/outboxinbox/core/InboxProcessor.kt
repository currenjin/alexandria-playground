package com.currenjin.outboxinbox.core

class InboxProcessor(
    private val inboxStore: InboxStore,
) {
    val log = mutableListOf<String>()

    fun process(message: Message, handler: (Message) -> Unit) {
        if (inboxStore.exists(message.id)) {
            val existing = inboxStore.find(message.id)!!
            when (existing.status) {
                InboxStatus.COMPLETED -> {
                    log.add("[Inbox] 중복 메시지 스킵: ${message.eventType} (${message.id})")
                    return
                }
                InboxStatus.PROCESSING -> {
                    log.add("[Inbox] 처리 중인 메시지 스킵: ${message.eventType} (${message.id})")
                    return
                }
                InboxStatus.FAILED -> {
                    log.add("[Inbox] 실패한 메시지 재처리: ${message.eventType} (${message.id})")
                }
            }
        }

        val record = InboxRecord(
            messageId = message.id,
            eventType = message.eventType,
            status = InboxStatus.PROCESSING,
        )
        inboxStore.save(record)

        try {
            handler(message)
            inboxStore.markCompleted(message.id)
            log.add("[Inbox] 메시지 처리 완료: ${message.eventType} (${message.id})")
        } catch (e: Exception) {
            inboxStore.markFailed(message.id, e.message ?: "알 수 없는 오류")
            log.add("[Inbox] 메시지 처리 실패: ${message.eventType} (${message.id}) - ${e.message}")
            throw e
        }
    }
}
