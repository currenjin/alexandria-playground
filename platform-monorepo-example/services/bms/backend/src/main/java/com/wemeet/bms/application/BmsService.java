package com.wemeet.bms.application;

import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.event.publish.EventPublisher;
import com.wemeet.contract.SettlementContracts.CreateSettlement;
import com.wemeet.contract.SettlementContracts.SettlementCompleted;
import com.wemeet.bms.domain.Settlement;
import com.wemeet.bms.domain.SettlementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 정산 유스케이스. orchestrator가 배송 완료를 받아 보낸 CreateSettlement 커맨드를 소비해
 * 정산을 생성(즉시 완료)하고 SettlementCompleted 를 발행한다. dispatchId 기준 멱등.
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
    void onCreateSettlement(CreateSettlement cmd) {
        var existing = settlements.findByDispatchId(cmd.dispatchId());
        if (existing.isPresent()) {
            Settlement s = existing.get();
            events.publish(new SettlementCompleted(s.settlementId(), cmd.dispatchId(), cmd.orderId(), s.amount()));
            log.info("이미 정산됨(멱등) dispatch={} 재통지", cmd.dispatchId());
            return;
        }
        String settlementId = "STL-" + UUID.randomUUID().toString().substring(0, 8);
        settlements.save(new Settlement(settlementId, cmd.dispatchId(), cmd.orderId(), cmd.amount(), Settlement.COMPLETED));
        events.publish(new SettlementCompleted(settlementId, cmd.dispatchId(), cmd.orderId(), cmd.amount()));
        log.info("정산 완료 {} (dispatch={} order={} amount={})", settlementId, cmd.dispatchId(), cmd.orderId(), cmd.amount());
    }
}
