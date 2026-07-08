package com.wemeet.eventbackbone.oms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 어댑터 (api 레이어). OMS 서비스의 Kafka 소비 진입점.
 * 구독 그룹·토픽은 설정(platform.events.consumer-group / subscribe-topics)에서 온다 — 하드코딩 없음.
 * OMS는 사가를 모른다 — 흐름 신호는 중앙 orchestrator가 구독한다(§7.1.7).
 */
@Component
public class OmsEventListener {

    private final EventConsumerSupport support;
    private final String group;

    public OmsEventListener(EventConsumerSupport support,
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
