package com.wemeet.eventbackbone.oms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 어댑터 (api 레이어). OMS 서비스의 Kafka 소비 진입점.
 * "oms" 그룹으로 자기 커맨드(oms.cmd, 예: 보상 커맨드 cancel_order)만 구독.
 * OMS는 사가를 모른다 — 흐름 신호(oms.order/tms.trip/bms.settlement)는 중앙 orchestrator가 구독한다(§7.1.7).
 */
@Component
public class OmsEventListener {

    private final EventConsumerSupport support;

    public OmsEventListener(EventConsumerSupport support) { this.support = support; }

    @KafkaListener(id = "oms", groupId = "oms", topics = {"oms.cmd"})
    public void oms(String envelope) { support.consume("oms", envelope); }
}
