package com.wemeet.eventbackbone.tms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** TMS 인바운드 어댑터 — 배차 커맨드 구독. */
@Component
public class TmsEventListener {

    private final EventConsumerSupport support;

    public TmsEventListener(EventConsumerSupport support) { this.support = support; }

    @KafkaListener(id = "tms", groupId = "tms", topics = {"tms.cmd"})
    public void tms(String envelope) { support.consume("tms", envelope); }
}
