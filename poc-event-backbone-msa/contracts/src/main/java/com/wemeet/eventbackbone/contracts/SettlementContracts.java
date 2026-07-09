package com.wemeet.eventbackbone.contracts;

/**
 * 공유 contracts — BMS 소유. 배송이 완료된 배차의 운임 정산.
 * 이벤트(bms.settlement.*)는 BMS가 발행, 커맨드(bms.cmd.*)는 orchestrator가 발행하고 BMS가 소비한다.
 */
public final class SettlementContracts {
    private SettlementContracts() {}

    // ── 이벤트(사실) bms.settlement.* ──
    @EventContract(type = "bms.settlement.completed", version = 1)
    public record SettlementCompleted(String settlementId, String dispatchId, String orderId, String amount)
            implements DomainEvent {
        @Override public String aggregateId() { return settlementId; }
    }

    // ── 커맨드(지시) bms.cmd.* — orchestrator 발행, BMS 소비 ──
    @EventContract(type = "bms.cmd.create_settlement", version = 1)
    public record CreateSettlement(String dispatchId, String orderId, String amount) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }
}
