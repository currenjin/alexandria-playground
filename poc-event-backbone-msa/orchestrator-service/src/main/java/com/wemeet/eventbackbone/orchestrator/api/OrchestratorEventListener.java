package com.wemeet.eventbackbone.orchestrator.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 어댑터 (api 레이어). 중앙 사가의 Kafka 소비 진입점.
 * "saga" 그룹으로 흐름 신호(이벤트)를 구독 — 참여자 서비스들이 발행한 이벤트를 사가가 받아 흐름을 진전시킨다.
 * (커맨드는 사가가 각 참여자 토픽 oms.cmd/tms.cmd/bms.cmd로 발행 → 각 서비스가 자기 그룹으로 소비)
 */
@Component
public class OrchestratorEventListener {

    private final EventConsumerSupport support;

    public OrchestratorEventListener(EventConsumerSupport support) { this.support = support; }

    @KafkaListener(id = "saga", groupId = "saga", topics = {"oms.order", "tms.trip", "bms.settlement"})
    public void saga(String envelope) { support.consume("saga", envelope); }
}
