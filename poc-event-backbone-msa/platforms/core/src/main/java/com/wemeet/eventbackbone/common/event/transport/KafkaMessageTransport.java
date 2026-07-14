package com.wemeet.eventbackbone.common.event.transport;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Kafka 전송 어댑터. acks 대기까지 동기로 보내고, 실패하면 예외를 던져 릴레이가 순서를 지키며 멈추게 한다.
 * (SQS로 갈 땐 이 자리에 SqsMessageTransport를 두면 된다.)
 */
@Component
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "kafka", matchIfMissing = true)
public class KafkaMessageTransport implements MessageTransport {

    private final KafkaTemplate<String, String> kafka;

    public KafkaMessageTransport(KafkaTemplate<String, String> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void send(String topic, String key, String payload) {
        try {
            kafka.send(topic, key, payload).get();
        } catch (Exception e) {
            throw new IllegalStateException("Kafka 발행 실패: " + topic, e);
        }
    }
}
