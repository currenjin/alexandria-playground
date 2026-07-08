package com.wemeet.eventbackbone.common.event.transport;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * SQS 수신 어댑터 — 스켈레톤(broker=sqs), {@link KafkaEventSubscriber}와 대칭.
 * 실제 구현은 Spring Cloud AWS @SqsListener 또는 SqsMessageListenerContainerFactory로
 * subscribe-topics(=큐)를 구독해 support.consume(group, payload)를 호출한다.
 */
@Component
@ConditionalOnExpression("'${platform.events.broker:kafka}'.equals('sqs') and !'${platform.events.subscribe-topics:}'.isEmpty()")
public class SqsEventSubscriber {

    private static final Logger log = LoggerFactory.getLogger(SqsEventSubscriber.class);

    public SqsEventSubscriber(EventConsumerSupport support,
                              @Value("${platform.events.consumer-group}") String group,
                              @Value("${platform.events.subscribe-topics}") String subscribeTopics) {
        // TODO(SQS): 각 topic(=큐)에 SqsMessageListenerContainer 등록 -> support.consume(group, payload).
        log.warn("SqsEventSubscriber 스켈레톤 — 미구현. group={} topics={} (Spring Cloud AWS 필요)", group, subscribeTopics);
    }
}
