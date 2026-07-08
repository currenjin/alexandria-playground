package com.wemeet.eventbackbone.common.event;

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
 * 이벤트 인프라 배선 (§7.1.6 재시도→DLT 포함).
 */
@Configuration
public class EventInfraConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * §7.1.6 — 인메모리 백오프 3회(1s→4s→16s) 후 <원본>.DLT로 produce.
     * DLT는 Kafka 브로커 기능이 아니라 컨슈머가 스스로 발행 = DeadLetterPublishingRecoverer.
     */
    @Bean
    public DefaultErrorHandler errorHandler(
            KafkaTemplate<String, String> kafkaTemplate,
            @Value("${platform.events.retry.max-attempts:3}") int maxRetries,
            @Value("${platform.events.retry.backoff-ms:1000}") long initialIntervalMs,
            @Value("${platform.events.retry.multiplier:4.0}") double multiplier) {

        // DLT는 Kafka 브로커 기능이 아니라 컨슈머가 스스로 <원본>.DLT로 produce (§7.1.6).
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", -1));

        // §7.1.6 인메모리 지수 백오프 3회 (1s → 4s → 16s) → DLT.
        // (ExponentialBackOffWithMaxRetries는 이 Spring 버전에 없어 ExponentialBackOff + maxElapsedTime으로 N회 캡)
        double sum = 0, interval = initialIntervalMs;
        for (int k = 0; k < maxRetries; k++) { sum += interval; interval *= multiplier; }  // 1000+4000+16000
        var backoff = new ExponentialBackOff(initialIntervalMs, multiplier);
        backoff.setMaxElapsedTime((long) sum);           // maxRetries회 후 STOP → recoverer(DLT)

        var handler = new DefaultErrorHandler(recoverer, backoff);
        handler.addNotRetryableExceptions(              // 재시도 무의미 → 즉시 DLT (§7.1.6)
                NonRetryableEventException.class, DeserializationException.class);
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
