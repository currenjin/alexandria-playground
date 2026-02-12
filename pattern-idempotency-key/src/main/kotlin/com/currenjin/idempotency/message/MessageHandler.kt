package com.currenjin.idempotency.message

class MessageHandler {
    val processedMessages = mutableListOf<String>()
    var shouldFail: Boolean = false

    fun handle(message: Message): MessageResult {
        if (shouldFail) {
            throw RuntimeException("메시지 처리 실패: ${message.messageId}")
        }
        processedMessages.add(message.messageId)
        return MessageResult(
            messageId = message.messageId,
            processed = true,
            output = "${message.topic} 처리 완료: ${message.payload}",
        )
    }
}
