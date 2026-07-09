package com.wemeet.eventbackbone.common.event.transport;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 브로커 어댑터(수신, Kafka) — 발신 포트 {@link MessageTransport}와 대칭.
 *
 * <p>서비스는 리스너를 갖지 않는다. 이 공통 어댑터 하나가 <b>@KafkaListener</b>로 설정
 * (platform.events.consumer-group / subscribe-topics)의 토픽을 구독해 공통 소비 파이프라인
 * {@link EventConsumerSupport#consume}로 넘긴다. 에러·재시도·DLT는 공통
 * {@code kafkaListenerContainerFactory}(DefaultErrorHandler)가 처리한다 — 여기서 컨테이너를 배선하지 않는다.
 *
 * <p>활성 조건 = broker가 kafka(기본)이고 subscribe-topics가 선언된 모듈에서만(소비 opt-in).
 * 브로커 교체 시 이 어댑터만 갈아끼운다(broker=inmemory → InMemoryEventSubscriber, sqs → SqsEventSubscriber).
 */
@Component
@ConditionalOnExpression("'${platform.events.broker:kafka}'.equals('kafka') and !'${platform.events.subscribe-topics:}'.isEmpty()")
public class KafkaEventSubscriber {

    private final EventConsumerSupport support;
    private final String group;

    public KafkaEventSubscriber(EventConsumerSupport support,
                                @Value("${platform.events.consumer-group}") String group) {
        this.support = support;
        this.group = group;
    }

    @KafkaListener(id = "${platform.events.consumer-group}",
                   groupId = "${platform.events.consumer-group}",
                   topics = "#{'${platform.events.subscribe-topics}'.split(',')}")
    public void onMessage(String envelope) {
        support.consume(group, envelope);
    }
}
