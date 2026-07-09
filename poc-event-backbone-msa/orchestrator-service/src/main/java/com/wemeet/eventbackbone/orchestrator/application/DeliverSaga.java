package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchDelivered;
import com.wemeet.eventbackbone.contracts.OrderContracts.DeliverOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderDelivered;
import com.wemeet.eventbackbone.contracts.OrderContracts.SettleOrder;
import com.wemeet.eventbackbone.contracts.SettlementContracts.CreateSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 배송완료 사가 (#5) — 무상태. 배송 사실 → 오더 배송완료 → 정산 생성 → 오더 정산완료.
 * <pre>
 *   DispatchDelivered(dispatchId, orderId)          → DeliverOrder(orderId, dispatchId)
 *   OrderDelivered(orderId, dispatchId, amount)     → CreateSettlement(dispatchId, orderId, amount)  // amount는 오더(권위)가 실어줌
 *   SettlementCompleted(settlementId, orderId)      → SettleOrder(orderId, settlementId)
 * </pre>
 * 배송은 물리적으로 되돌릴 수 없으므로 보상 없음 — 각 단계는 재시도로 수렴(정산 실패는 재시도, 순서 역전은 OMS가 재시도).
 */
@Component
public class DeliverSaga {

    private static final Logger log = LoggerFactory.getLogger(DeliverSaga.class);
    private final EventPublisher events;

    public DeliverSaga(EventPublisher events) {
        this.events = events;
    }

    @EventHandler
    void onDispatchDelivered(DispatchDelivered e) {
        events.publish(new DeliverOrder(e.orderId(), e.dispatchId()));
        log.info("[deliver-saga {}] 배송완료 → 오더 배송완료 전이 시도(dispatch={})", e.orderId(), e.dispatchId());
    }

    @EventHandler
    void onOrderDelivered(OrderDelivered e) {
        events.publish(new CreateSettlement(e.dispatchId(), e.orderId(), e.amount()));
        log.info("[deliver-saga {}] 오더 배송완료 → 정산 생성 지시(amount={})", e.orderId(), e.amount());
    }

    @EventHandler
    void onSettlementCompleted(SettlementCompleted e) {
        events.publish(new SettleOrder(e.orderId(), e.settlementId()));
        log.info("[deliver-saga {}] 정산 완료 → 오더 정산완료 전이 시도(settlement={})", e.orderId(), e.settlementId());
    }
}
