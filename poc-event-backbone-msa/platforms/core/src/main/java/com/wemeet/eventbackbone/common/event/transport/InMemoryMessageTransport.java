package com.wemeet.eventbackbone.common.event.transport;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** 인메모리 발신 어댑터 (broker=inmemory) — {@link MessageTransport} 구현, 버스로 발행. Kafka 어댑터와 동급. */
@Component
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "inmemory")
public class InMemoryMessageTransport implements MessageTransport {

    private final InMemoryBus bus;

    public InMemoryMessageTransport(InMemoryBus bus) {
        this.bus = bus;
    }

    @Override
    public void send(String topic, String key, String payload) {
        bus.publish(topic, payload);
    }
}
