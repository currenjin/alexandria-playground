package com.wemeet.eventbackbone.common.event.transport;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 인메모리 수신 어댑터 (broker=inmemory) — {@link KafkaEventSubscriber}와 대칭.
 * 설정(consumer-group / subscribe-topics)의 토픽을 버스에서 구독해 공통 소비 파이프라인으로 넘긴다.
 */
@Component
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "inmemory")
public class InMemoryEventSubscriber {

    public InMemoryEventSubscriber(InMemoryBus bus, EventConsumerSupport support,
                                   @Value("${platform.events.consumer-group}") String group,
                                   @Value("${platform.events.subscribe-topics}") String subscribeTopics) {
        for (String topic : subscribeTopics.split(",")) {
            bus.subscribe(topic.trim(), value -> support.consume(group, value));
        }
    }
}
