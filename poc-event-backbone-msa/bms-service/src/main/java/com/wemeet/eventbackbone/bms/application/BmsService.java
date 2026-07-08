package com.wemeet.eventbackbone.bms.application;

import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.event.HandlerRegistry;
import com.wemeet.eventbackbone.contracts.SettlementContracts.ScheduleSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementScheduled;
import com.wemeet.eventbackbone.bms.domain.Settlement;
import com.wemeet.eventbackbone.bms.domain.SettlementRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 정산 유스케이스: 정산 예정 커맨드 처리(step).
 */
@Service
public class BmsService {

    private static final Logger log = LoggerFactory.getLogger(BmsService.class);

    private final SettlementRepository settlements;
    private final EventPublisher events;
    private final HandlerRegistry registry;

    public BmsService(SettlementRepository settlements, EventPublisher events, HandlerRegistry registry) {
        this.settlements = settlements;
        this.events = events;
        this.registry = registry;
    }

    @PostConstruct
    void register() {
        registry.register("bms", ScheduleSettlement.class, this::onScheduleSettlement);
    }

    void onScheduleSettlement(ScheduleSettlement cmd) {
        String settlementId = "STL-" + UUID.randomUUID().toString().substring(0, 8);
        settlements.save(new Settlement(settlementId, cmd.tripId(), cmd.amount(), Settlement.SCHEDULED));
        events.publish(new SettlementScheduled(settlementId, cmd.tripId()));
        log.info("정산 예정 {} (trip={})", settlementId, cmd.tripId());
    }
}
