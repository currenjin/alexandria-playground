package com.wemeet.eventbackbone.common.event;

import com.wemeet.eventbackbone.contracts.DomainEvent;

/**
 * 발행 포트 (§7.1.1). 개발자는 이 한 줄만: publish(event).
 * 구현체(OutboxEventPublisher)는 봉투 조립 + OUTBOX INSERT를 도메인 트랜잭션에 합류시킨다.
 * 테스트는 InMemory 구현으로 교체 (§7.1.8).
 */
public interface EventPublisher {
    void publish(DomainEvent event);
}
