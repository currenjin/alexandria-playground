package com.wemeet.eventbackbone.contracts;

/** 공유 contracts — BMS 소유. */
public final class SettlementContracts {
    private SettlementContracts() {}

    @EventContract(type = "bms.cmd.schedule_settlement", version = 1)
    public record ScheduleSettlement(String tripId, String amount) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }

    @EventContract(type = "bms.settlement.scheduled", version = 1)
    public record SettlementScheduled(String settlementId, String tripId) implements DomainEvent {
        @Override public String aggregateId() { return settlementId; }
    }
}
