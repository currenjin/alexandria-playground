package com.wemeet.orchestrator.application;

import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.contract.OrderContracts.OrderCancelled;
import com.wemeet.contract.OrderContracts.OrderSettled;
import com.wemeet.contract.OrderContracts.OrderUndispatched;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 사가 생애 종결 — 오더 <b>종료 사실</b>을 받아 saga_instance를 finish로 마감한다(timeout_at 해제 → 스캐너 오탐 방지).
 * 이 종료 이벤트(OrderSettled·OrderCancelled·OrderUndispatched)는 다른 사가가 구독하지 않으므로
 * (consumerGroup, eventType) 유니크 제약과 충돌 없다. saga_instance가 없는 orderId면 finish는 0행 UPDATE(무해).
 */
@Component
public class SagaLifecycle {

    private static final Logger log = LoggerFactory.getLogger(SagaLifecycle.class);

    private final SagaStore saga;

    public SagaLifecycle(SagaStore saga) {
        this.saga = saga;
    }

    @EventHandler
    void onOrderSettled(OrderSettled e) {
        saga.finish(e.orderId(), "SETTLED");
        log.info("[saga-lifecycle {}] 정산완료 → 프로세스 종결(SETTLED)", e.orderId());
    }

    @EventHandler
    void onOrderCancelled(OrderCancelled e) {
        saga.finish(e.orderId(), "CANCELLED");
        log.info("[saga-lifecycle {}] 취소 → 프로세스 종결(CANCELLED)", e.orderId());
    }

    @EventHandler
    void onOrderUndispatched(OrderUndispatched e) {
        saga.finish(e.orderId(), "UNDISPATCHED");
        log.info("[saga-lifecycle {}] 미배차 복귀 → 프로세스 종결(UNDISPATCHED)", e.orderId());
    }
}
