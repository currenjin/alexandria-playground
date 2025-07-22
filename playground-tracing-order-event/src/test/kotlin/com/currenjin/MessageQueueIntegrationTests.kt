package com.currenjin

import com.currenjin.event.OrderEvent
import com.currenjin.event.PaymentEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MessageQueueIntegrationTests {
    private val messageQueue = MessageQueue()
    private val publisher = Publisher(messageQueue)

    private val orderEvent = OrderEvent("orderId", "userId", BigDecimal(10.0), "CREATED")
    private val paymentEvent = PaymentEvent("orderId", BigDecimal(10.0), "PAID")

    @Test
    fun different_types_of_subscribers_test() {
        val orderSubscriber = Subscriber(messageQueue = messageQueue, targetEvent = OrderEvent::class)
        val paymentSubscriber = Subscriber(messageQueue = messageQueue, targetEvent = PaymentEvent::class)

        publisher.publish(createMessageWith(orderEvent))
        orderSubscriber.subscribe { event: Message ->
            assertThat(event.type)
                .isEqualTo(OrderEvent::class.simpleName)
        }

        publisher.publish(createMessageWith(paymentEvent))
        paymentSubscriber.subscribe { event: Message ->
            assertThat(event.type)
                .isEqualTo(PaymentEvent::class.simpleName)
        }
    }

    @Test
    fun retry_message_test() {
        var tryCount = 0
        val subscriber = Subscriber(messageQueue = messageQueue, targetEvent = OrderEvent::class)

        publisher.publish(createMessageWith(orderEvent))

        subscriber.subscribe { event: Message ->
            tryCount++
            throw RuntimeException(event.toString())
        }

        Thread.sleep(3000)
        assertThat(tryCount).isEqualTo(3)
        assertThat(messageQueue.getStatus()).contains("Messages: 0")
    }

    private fun createMessageWith(event: Any) = Message(payload = event, type = event.javaClass.simpleName)
}
