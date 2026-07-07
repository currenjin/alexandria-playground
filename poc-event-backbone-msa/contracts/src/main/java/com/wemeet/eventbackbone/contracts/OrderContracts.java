package com.wemeet.eventbackbone.contracts;

/**
 * 공유 contracts (§7.1.7 의존 한 방향) — OMS 소유 이벤트/커맨드.
 * 사가·타 서비스는 이 record만 참조, OMS 내부 메소드는 모른다.
 */
public final class OrderContracts {
    private OrderContracts() {}

    // 이벤트 (사건) — 금액=문자열+currency (§7.1.1)
    @EventContract(type = "oms.order.confirmed", version = 1)
    public record OrderConfirmed(String orderId, String customerId, String amount, String currency)
            implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @EventContract(type = "oms.order.cancelled", version = 1)
    public record OrderCancelled(String orderId, String reason) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    // 커맨드 (지시) — 사가가 OMS에 보냄
    @EventContract(type = "oms.cmd.cancel_order", version = 1)
    public record CancelOrder(String orderId, String reason) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }
}
