package com.wemeet.contract;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventContract;

/**
 * 공유 contracts — OMS 소유. 오더 = 화주 운송요청 + **생애주기 상태의 단일 권위(authority)**.
 * 오더 상태 전이는 OMS 애그리거트가 낙관적 잠금 + 가드로 판정한다 — 유효하면 성공 사실을, 아니면 거부 사실을 발행.
 * 커맨드(oms.cmd.*)는 액션별 사가(orchestrator)가 보내는 "전이 시도"다.
 */
public final class OrderContracts {
    private OrderContracts() {}

    // ── 사실(fact) oms.order.* ──
    @EventContract(type = "oms.order.created", version = 1)
    public record OrderCreated(String orderId, String shipperId, String origin, String destination,
                               String amount, String currency) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.dispatched", version = 1)
    public record OrderDispatched(String orderId, String dispatchId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.undispatched", version = 1)
    public record OrderUndispatched(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.delivered", version = 1)
    public record OrderDelivered(String orderId, String dispatchId, String amount) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.settled", version = 1)
    public record OrderSettled(String orderId, String settlementId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.cancelled", version = 1)
    public record OrderCancelled(String orderId, String reason) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    /** 배차 전이 거부(오더가 CREATED가 아님) — 배차확정 사가가 이걸 받고 배차를 보상 취소한다. */
    @EventContract(type = "oms.order.dispatch_rejected", version = 1)
    public record OrderDispatchRejected(String orderId, String dispatchId, String currentStatus) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    /** "배차된 오더는 취소 불가" 규칙 위반 — 사용자 취소 거부(관측용 사실). */
    @EventContract(type = "oms.order.cancel_rejected", version = 1)
    public record OrderCancelRejected(String orderId, String reason, String currentStatus) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    // ── 커맨드(전이 시도) oms.cmd.* — 사가가 발행, OMS 애그리거트가 가드로 판정 ──
    @EventContract(type = "oms.cmd.dispatch_order", version = 1)
    public record DispatchOrder(String orderId, String dispatchId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.cmd.undispatch_order", version = 1)
    public record UndispatchOrder(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.cmd.deliver_order", version = 1)
    public record DeliverOrder(String orderId, String dispatchId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.cmd.settle_order", version = 1)
    public record SettleOrder(String orderId, String settlementId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }
}
