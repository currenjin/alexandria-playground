package com.wemeet.eventbackbone.testsupport;

import com.wemeet.eventbackbone.common.event.contract.DomainEvent;
import com.wemeet.eventbackbone.common.event.publish.EventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 소비 앱용 테스트 하네스 — 인프라(Kafka·DB) 없이 "무엇이 발행됐나"만 검증하는 {@link EventPublisher} 더블.
 * 비즈 로직 단위 테스트에서 실제 발행자(OutboxEventPublisher) 대신 주입한다.
 * 사용: {@code testImplementation testFixtures(project(':platforms:core'))}
 */
public class FakeEventPublisher implements EventPublisher {

    public final List<DomainEvent> published = new ArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        published.add(event);
    }

    /** 발행된 것 중 해당 타입 첫 이벤트. */
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> Optional<T> first(Class<T> type) {
        return published.stream().filter(type::isInstance).map(e -> (T) e).findFirst();
    }

    /** 해당 타입이 몇 번 발행됐나. */
    public long count(Class<? extends DomainEvent> type) {
        return published.stream().filter(type::isInstance).count();
    }

    public void clear() {
        published.clear();
    }
}
