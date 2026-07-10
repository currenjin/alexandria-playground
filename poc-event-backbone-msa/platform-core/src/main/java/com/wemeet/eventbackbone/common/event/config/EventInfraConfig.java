package com.wemeet.eventbackbone.common.event.config;

import com.wemeet.eventbackbone.common.event.consume.NonRetryableEventException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * 이벤트 인프라 배선: ObjectMapper, DLT 에러 핸들러, Kafka 리스너 팩토리.
 */
@Configuration
public class EventInfraConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * 핸들러 실패 시 인메모리 지수 백오프로 재시도(1s → 4s → 16s)하고, 소진되면 &lt;원본&gt;.DLT로 produce한다.
     * DLT는 Kafka 브로커 기능이 아니라 컨슈머가 스스로 발행하는 것(DeadLetterPublishingRecoverer).
     * 역직렬화 실패처럼 재시도가 무의미한 예외는 즉시 DLT로 보낸다.
     */
    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${platform.events.retry.max-attempts:3}") int maxRetries,
            @Value("${platform.events.retry.backoff-ms:1000}") long initialIntervalMs,
            @Value("${platform.events.retry.multiplier:4.0}") double multiplier) {

        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", -1));

        double sum = 0, interval = initialIntervalMs;
        for (int k = 0; k < maxRetries; k++) { sum += interval; interval *= multiplier; }
        var backoff = new ExponentialBackOff(initialIntervalMs, multiplier);
        backoff.setMaxElapsedTime((long) sum);

        var handler = new DefaultErrorHandler(recoverer, backoff);
        handler.addNotRetryableExceptions(NonRetryableEventException.class, DeserializationException.class);
        return handler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory, DefaultErrorHandler errorHandler) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
