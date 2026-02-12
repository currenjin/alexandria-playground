package com.currenjin.idempotency.message

import com.currenjin.idempotency.core.IdempotencyExecutor
import com.currenjin.idempotency.core.IdempotencyKey
import com.currenjin.idempotency.core.IdempotencyResult
import com.currenjin.idempotency.core.InMemoryIdempotencyStore
import java.time.Duration

class IdempotentMessageProcessor(
    private val handler: MessageHandler = MessageHandler(),
    ttl: Duration = Duration.ofHours(1),
) {
    private val store = InMemoryIdempotencyStore<MessageResult>()
    private val executor = IdempotencyExecutor(store, ttl = ttl)

    val log get() = executor.log

    fun process(message: Message): IdempotencyResult<MessageResult> {
        val key = IdempotencyKey.from("message", message.messageId)
        return executor.execute(key) {
            handler.handle(message)
        }
    }

    val processedCount get() = handler.processedMessages.size
}
