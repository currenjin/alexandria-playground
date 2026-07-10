package com.wemeet.eventbackbone.oms.support;

import com.wemeet.eventbackbone.common.event.publish.EventPublisher;
import com.wemeet.eventbackbone.common.event.contract.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 테스트 더블 (§7.1.8 1층) — 인프라(Kafka·DB) 없이 "무엇이 발행됐나"만 검증.
 * 비즈 로직 단위 테스트에서 OutboxEventPublisher 대신 주입한다.
 */
public class FakeEventPublisher implements EventPublisher {

    public final List<DomainEvent> published = new ArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        published.add(event);
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> Optional<T> first(Class<T> type) {
        return published.stream().filter(type::isInstance).map(e -> (T) e).findFirst();
    }
}
