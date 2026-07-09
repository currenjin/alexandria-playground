package com.wemeet.eventbackbone.contracts;

/**
 * 공유 contracts — OMS 소유. 루티프로 미들마일: 오더 = 1상차→1하차 운송 건.
 * 이벤트(사실, oms.order.*)는 OMS가 발행. 커맨드(지시, oms.cmd.*)는 orchestrator가 발행하고 OMS가 소비한다
 * — 오더 <b>상태 동기화</b>용(배차/배송/정산/미배차 복귀). 오더 생성·취소는 OMS 자기 API로 직접 수행한다(커맨드 아님).
 */
public final class OrderContracts {
    private OrderContracts() {}

    // ── 이벤트(사실) oms.order.* ──
    @EventContract(type = "oms.order.created", version = 1)
    public record OrderCreated(String orderId, String shipperId, String origin, String destination,
                               String amount, String currency) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.cancelled", version = 1)
    public record OrderCancelled(String orderId, String reason) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    /** "배차된 오더는 취소 불가" 규칙 위반 — OMS 애그리거트가 자체 판정해 발행(관측용 사실). */
    @EventContract(type = "oms.order.cancel_rejected", version = 1)
    public record OrderCancelRejected(String orderId, String reason, String currentStatus) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    // ── 커맨드(지시) oms.cmd.* — orchestrator 발행, OMS 소비 (오더 상태 동기화) ──
    @EventContract(type = "oms.cmd.mark_dispatched", version = 1)
    public record MarkDispatched(String orderId, String dispatchId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.cmd.mark_delivered", version = 1)
    public record MarkDelivered(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.cmd.mark_settled", version = 1)
    public record MarkSettled(String orderId, String settlementId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    /** 배차 취소로 오더를 다시 미배차(CREATED)로 되돌리는 지시. */
    @EventContract(type = "oms.cmd.mark_undispatched", version = 1)
    public record MarkUndispatched(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }
}
