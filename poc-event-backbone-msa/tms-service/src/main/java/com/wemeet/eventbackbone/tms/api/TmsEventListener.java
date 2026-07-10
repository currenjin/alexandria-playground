package com.wemeet.eventbackbone.tms.api;

import com.wemeet.eventbackbone.common.event.consume.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * TMS 인바운드 어댑터 — 이 모듈이 소유하는 컨슈머. TMS는 주로 자기 API로 행위(publish)하지만,
 * 사가 보상 커맨드({@code tms.cmd}의 CancelDispatch)만 소비한다. 설정(consumer-group·subscribe-topics) 주입.
 * 에러·재시도·DLT는 공통 {@code kafkaListenerContainerFactory}가 처리.
 */
@Component
@ConditionalOnExpression("'${platform.events.broker:kafka}'.equals('kafka') and !'${platform.events.subscribe-topics:}'.isEmpty()")
public class TmsEventListener {

    private final EventConsumerSupport support;
    private final String group;

    public TmsEventListener(EventConsumerSupport support,
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
