package com.wemeet.eventbackbone.contracts;

import com.wemeet.eventbackbone.common.event.contract.DomainEvent;
import com.wemeet.eventbackbone.common.event.contract.EventContract;

/**
 * 공유 contracts — BMS 소유. 배송 완료된 배차의 운임 정산. orchestrator의 CreateSettlement 커맨드를 소비해
 * 정산을 생성하고 SettlementCompleted 사실을 발행한다.
 */
public final class SettlementContracts {
    private SettlementContracts() {}

    // ── 사실(fact) bms.settlement.* ──
    @EventContract(type = "bms.settlement.completed", version = 1)
    public record SettlementCompleted(String settlementId, String dispatchId, String orderId, String amount)
            implements DomainEvent {
        @Override public String aggregateId() { return settlementId; }
    }

    // ── 커맨드 bms.cmd.* — 배송완료 사가가 발행 ──
    @EventContract(type = "bms.cmd.create_settlement", version = 1)
    public record CreateSettlement(String dispatchId, String orderId, String amount) implements DomainEvent {
        @Override public String aggregateId() { return dispatchId; }
    }
}
