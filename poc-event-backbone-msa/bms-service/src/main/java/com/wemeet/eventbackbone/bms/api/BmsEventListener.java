package com.wemeet.eventbackbone.bms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** BMS 인바운드 어댑터 — 정산 커맨드 구독. 그룹·토픽은 설정에서 주입(하드코딩 없음). */
@Component
public class BmsEventListener {

    private final EventConsumerSupport support;
    private final String group;

    public BmsEventListener(EventConsumerSupport support,
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
