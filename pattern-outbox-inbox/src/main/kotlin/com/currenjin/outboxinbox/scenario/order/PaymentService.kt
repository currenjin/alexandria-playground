package com.currenjin.outboxinbox.scenario.order

import com.currenjin.outboxinbox.core.InboxProcessor
import com.currenjin.outboxinbox.core.Message
import com.currenjin.outboxinbox.core.MessageBroker
import java.util.UUID

class PaymentService(
    private val paymentStore: PaymentStore,
    private val inboxProcessor: InboxProcessor,
    private val messageBroker: MessageBroker,
) {
    val log get() = inboxProcessor.log
    var processedCount = 0
        private set

    fun subscribe() {
        messageBroker.subscribe(OrderCreatedEvent.EVENT_TYPE) { message ->
            processMessage(message)
        }
    }

    fun processMessage(message: Message) {
        inboxProcessor.process(message) { msg ->
            val event = OrderCreatedEvent.fromPayload(msg.payload)
            val payment = Payment(
                id = UUID.randomUUID().toString(),
                orderId = event.orderId,
                amount = event.amount,
            )
            paymentStore.save(payment)
            processedCount++
        }
    }
}
