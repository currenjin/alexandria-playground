package com.wemeet.eventbackbone.common.event.transport;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.MessageListener;

/**
 * 인바운드 브로커 어댑터(수신) — 발신 포트 {@link MessageTransport}와 대칭.
 *
 * <p>서비스는 {@code @KafkaListener}를 두지 않는다. 이 공통 어댑터가 설정
 * (platform.events.consumer-group / subscribe-topics)만 보고 Kafka 리스너 컨테이너를
 * 프로그램적으로 등록하고, 도착 메시지를 공통 소비 파이프라인 {@link EventConsumerSupport}로 넘긴다.
 * 컨테이너는 SmartLifecycle이라 스프링이 기동/종료를 자동 관리한다.
 *
 * <p>브로커 교체 = 이 어댑터만 갈아끼운다. Kafka는 {@code platform.events.broker=kafka}(기본),
 * SQS로 가면 {@code broker=sqs}용 SqsEventSubscriber를 추가하고 이 빈은 비활성된다 —
 * 서비스 코드(도메인·핸들러)는 브로커를 전혀 모른다.
 */
@Configuration
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "kafka", matchIfMissing = true)
public class KafkaEventSubscriber {

    @Bean
    public ConcurrentMessageListenerContainer<String, String> eventListenerContainer(
            ConsumerFactory<String, String> consumerFactory,
            DefaultErrorHandler errorHandler,
            EventConsumerSupport support,
            @Value("${platform.events.consumer-group}") String group,
            @Value("${platform.events.subscribe-topics}") String subscribeTopics) {

        ContainerProperties props = new ContainerProperties(subscribeTopics.split(","));
        props.setGroupId(group);
        props.setMessageListener((MessageListener<String, String>) rec -> support.consume(group, rec.value()));

        var container = new ConcurrentMessageListenerContainer<>(consumerFactory, props);
        container.setCommonErrorHandler(errorHandler);
        container.setBeanName("event-subscriber-" + group);
        return container;
    }
}
