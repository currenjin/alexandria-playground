package com.wemeet.eventbackbone.tms.api;

import com.wemeet.eventbackbone.common.event.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * TMS 인바운드 어댑터 — 이 모듈이 소유하는 컨슈머(모듈이 자기 리스너를 명시적으로 가짐).
 *
 * <p>{@code @KafkaListener}로 설정(consumer-group·subscribe-topics)의 토픽을 구독해
 * 공통 소비 파이프라인 {@link EventConsumerSupport#consume}로 넘긴다(→ inbox 멱등 → {@code @EventHandler} 디스패치).
 * 그룹·토픽은 하드코딩하지 않고 yml에서 주입(환경별로 바뀌어도 코드 불변).
 * 에러·재시도·DLT는 공통 {@code kafkaListenerContainerFactory}(DefaultErrorHandler)가 처리한다.
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
