package com.wemeet.eventbackbone.contracts;

/**
 * 모든 도메인 이벤트/커맨드 record가 구현. envelope(§7.1.1)에서 개발자가 채우는 aggregateId를 노출한다.
 */
public interface DomainEvent {
    String aggregateId();
}
