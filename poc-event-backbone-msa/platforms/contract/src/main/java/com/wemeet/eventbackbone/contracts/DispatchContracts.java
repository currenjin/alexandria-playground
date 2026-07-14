package com.wemeet.eventbackbone.contracts;

import com.wemeet.eventbackbone.common.event.contract.DomainEvent;
import com.wemeet.eventbackbone.common.event.contract.EventContract;

/**
 * 공유 contracts — TMS 소유. 배차(dispatch). TMS는 자기 API(배차·배송완료·배차취소)로 행위하고
 * 결과 사실(tms.dispatch.*)을 발행한다. 커맨드는 하나뿐 — 사가 보상용 CancelDispatch(배차확정 사가가
 * 오더 전이 거부를 받으면 배차를 되돌리려고 보냄).
 */
public final class DispatchContracts {
    private DispatchContracts() {}

    // ── 사실(fact) tms.dispatch.* ──
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

    // ── 커맨드(사가 보상) tms.cmd.* ──
    @EventContract(type = "tms.cmd.cancel_dispatch", version = 1)
    public record CancelDispatch(String dispatchId, String orderId) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }
}
