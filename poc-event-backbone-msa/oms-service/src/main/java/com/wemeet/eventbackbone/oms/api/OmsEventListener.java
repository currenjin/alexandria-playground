package com.wemeet.eventbackbone.oms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 인바운드 어댑터 (api 레이어). OMS 서비스의 Kafka 소비 진입점.
 * - "saga" 그룹: 흐름 신호(이벤트) 구독 — 사가(흐름 주인)가 처리
 * - "oms" 그룹: 보상 커맨드(cancel_order) 구독
 */
@Component
public class OmsEventListener {

    private final EventConsumerSupport support;

    public OmsEventListener(EventConsumerSupport support) { this.support = support; }

    @KafkaListener(id = "saga", groupId = "saga", topics = {"oms.order", "tms.trip", "bms.settlement"})
    public void saga(String envelope) { support.consume("saga", envelope); }

    @KafkaListener(id = "oms", groupId = "oms", topics = {"oms.cmd"})
    public void oms(String envelope) { support.consume("oms", envelope); }
}
