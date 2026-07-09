package com.wemeet.eventbackbone.contracts;

/**
 * 공유 contracts — TMS 소유. 배차 = 오더에 차량/기사 배정 후 운송(상차→하차).
 * TMS는 자기 API(배차·배송완료·배차취소)로 행위하고 결과 <b>사실(tms.dispatch.*)</b>만 발행한다(커맨드 소비 없음).
 * orchestrator가 이 사실을 받아 오더 상태 동기화·정산 트리거를 코디네이션한다.
 */
public final class DispatchContracts {
    private DispatchContracts() {}

    // ── 이벤트(사실) tms.dispatch.* ──
    @EventContract(type = "tms.dispatch.created", version = 1)
    public record DispatchCreated(String dispatchId, String orderId, String carrierId) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }

    @EventContract(type = "tms.dispatch.delivered", version = 1)
    public record DispatchDelivered(String dispatchId, String orderId) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }

    @EventContract(type = "tms.dispatch.cancelled", version = 1)
    public record DispatchCancelled(String dispatchId, String orderId) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }
}
