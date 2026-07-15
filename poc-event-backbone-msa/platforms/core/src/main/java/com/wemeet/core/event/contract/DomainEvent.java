package com.wemeet.core.event.contract;

/**
 * 모든 도메인 이벤트/커맨드 record가 구현. envelope(§7.1.1)에서 개발자가 채우는 aggregateId를 노출한다.
 * 계약 메커니즘(라이브러리) — 예제 계약(contracts 모듈)이 이 타입을 구현한다.
 */
public interface DomainEvent {
    String aggregateId();
}
