package com.wemeet.eventbackbone.contracts;

/**
 * 모든 도메인 이벤트/커맨드 record가 구현. 봉투(§7.1.1)의 aggregateId만 개발자가 제공.
 */
public interface DomainEvent {
    String aggregateId();   // 파티션 키·"어느 건의 이벤트인가" (§7.1.1)
}
