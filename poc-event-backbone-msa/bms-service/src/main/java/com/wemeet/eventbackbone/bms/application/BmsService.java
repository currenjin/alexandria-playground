package com.wemeet.eventbackbone.bms.application;

import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.SettlementContracts.ScheduleSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementScheduled;
import com.wemeet.eventbackbone.bms.domain.Settlement;
import com.wemeet.eventbackbone.bms.domain.SettlementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 정산 유스케이스(step).
 */
@Service
public class BmsService {

    private static final Logger log = LoggerFactory.getLogger(BmsService.class);

    private final SettlementRepository settlements;
    private final EventPublisher events;

    public BmsService(SettlementRepository settlements, EventPublisher events) {
        this.settlements = settlements;
        this.events = events;
    }

    @EventHandler
    void onScheduleSettlement(ScheduleSettlement cmd) {
        var existing = settlements.findByTripId(cmd.tripId());
        if (existing.isPresent()) {
            events.publish(new SettlementScheduled(existing.get().settlementId(), cmd.tripId()));
            log.info("이미 정산예정(멱등) trip={} 재통지", cmd.tripId());
            return;
        }
        String settlementId = "STL-" + UUID.randomUUID().toString().substring(0, 8);
        settlements.save(new Settlement(settlementId, cmd.tripId(), cmd.amount(), Settlement.SCHEDULED));
        events.publish(new SettlementScheduled(settlementId, cmd.tripId()));
        log.info("정산 예정 {} (trip={})", settlementId, cmd.tripId());
    }
}
