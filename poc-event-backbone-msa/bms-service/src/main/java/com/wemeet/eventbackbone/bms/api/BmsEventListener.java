package com.wemeet.eventbackbone.bms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** BMS 인바운드 어댑터 — 정산 커맨드 구독. */
@Component
public class BmsEventListener {

    private final EventConsumerSupport support;

    public BmsEventListener(EventConsumerSupport support) { this.support = support; }

    @KafkaListener(id = "bms", groupId = "bms", topics = {"bms.cmd"})
    public void bms(String envelope) { support.consume("bms", envelope); }
}
