package com.wemeet.eventbackbone.orchestrator.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 어댑터 (api 레이어). 중앙 사가의 Kafka 소비 진입점.
 * 흐름 신호(이벤트)를 구독 — 그룹·토픽은 설정에서 주입(하드코딩 없음).
 * (커맨드는 사가가 각 참여자 토픽으로 발행 → 각 서비스가 자기 그룹으로 소비)
 */
@Component
public class OrchestratorEventListener {

    private final EventConsumerSupport support;
    private final String group;

    public OrchestratorEventListener(EventConsumerSupport support,
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
