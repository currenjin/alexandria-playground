package com.wemeet.eventbackbone.orchestrator.api;

import com.wemeet.eventbackbone.common.event.consume.EventConsumerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Orchestrator(사가) 인바운드 어댑터 — 이 모듈이 소유하는 컨슈머.
 *
 * <p>여러 서비스의 도메인 이벤트(oms.order·tms.dispatch·bms.settlement)를 한 그룹(saga)으로 구독해
 * 공통 소비 파이프라인 {@link EventConsumerSupport#consume}로 넘긴다(→ inbox 멱등 → {@code @EventHandler} 디스패치 → 사가 진행).
 * 그룹·토픽은 하드코딩하지 않고 yml에서 주입(환경별로 바뀌어도 코드 불변).
 * 에러·재시도·DLT는 공통 {@code kafkaListenerContainerFactory}(DefaultErrorHandler)가 처리한다.
 */
@Component
@ConditionalOnExpression("'${platform.events.broker:kafka}'.equals('kafka') and !'${platform.events.subscribe-topics:}'.isEmpty()")
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
