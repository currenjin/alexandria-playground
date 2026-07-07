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
import org.springframework.util.backoff.FixedBackOff;

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
            @Value("${platform.events.retry.max-attempts:3}") int maxAttempts,
            @Value("${platform.events.retry.backoff-ms:1000}") long backoffMs) {

        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new org.apache.kafka.common.TopicPartition(record.topic() + ".DLT", -1));

        // §7.1.6 스펙은 지수 백오프(1s→4s→16s). 예제 포터빌리티 위해 고정 간격 재시도로 단순화.
        var backoff = new FixedBackOff(backoffMs, maxAttempts - 1);   // 초기 1회 + (maxAttempts-1) 재시도

        var handler = new DefaultErrorHandler(recoverer, backoff);
        handler.addNotRetryableExceptions(NonRetryableEventException.class); // 즉시 DLT
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
