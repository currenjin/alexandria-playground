package com.example.imageworker.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    companion object {
        const val IMAGE_EXCHANGE = "image.exchange"
        const val IMAGE_UPLOADED_QUEUE = "image.uploaded"
        const val IMAGE_UPLOADED_ROUTING_KEY = "image.uploaded"
        const val IMAGE_DLQ = "image.uploaded.dlq"
        const val IMAGE_DLX = "image.dlx"
    }

    @Bean
    fun imageExchange(): DirectExchange {
        return DirectExchange(IMAGE_EXCHANGE)
    }

    @Bean
    fun dlxExchange(): DirectExchange {
        return DirectExchange(IMAGE_DLX)
    }

    @Bean
    fun imageUploadedQueue(): Queue {
        return QueueBuilder.durable(IMAGE_UPLOADED_QUEUE)
            .withArgument("x-dead-letter-exchange", IMAGE_DLX)
            .withArgument("x-dead-letter-routing-key", IMAGE_DLQ)
            .build()
    }

    @Bean
    fun deadLetterQueue(): Queue {
        return QueueBuilder.durable(IMAGE_DLQ).build()
    }

    @Bean
    fun imageUploadedBinding(): Binding {
        return BindingBuilder.bind(imageUploadedQueue())
            .to(imageExchange())
            .with(IMAGE_UPLOADED_ROUTING_KEY)
    }

    @Bean
    fun dlqBinding(): Binding {
        return BindingBuilder.bind(deadLetterQueue())
            .to(dlxExchange())
            .with(IMAGE_DLQ)
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun rabbitListenerContainerFactory(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(messageConverter)
        factory.setDefaultRequeueRejected(false)
        return factory
    }
}
