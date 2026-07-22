package com.wemeet.orchestrator.application;

import com.wemeet.core.context.FlowContext;
import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.event.publish.EventPublisher;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.contract.DispatchContracts.DispatchDelivered;
import com.wemeet.contract.OrderContracts.DeliverOrder;
import com.wemeet.contract.OrderContracts.OrderDelivered;
import com.wemeet.contract.OrderContracts.SettleOrder;
import com.wemeet.contract.SettlementContracts.CreateSettlement;
import com.wemeet.contract.SettlementContracts.SettlementCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * 배송완료 사가 (#5) — 무상태 반응 + 진행 기록. 배송 사실 → 오더 배송완료 → 정산 생성 → 오더 정산완료.
 * <pre>
 *   DispatchDelivered(dispatchId, orderId)          → DeliverOrder(orderId, dispatchId)
 *   OrderDelivered(orderId, dispatchId, amount)     → CreateSettlement(dispatchId, orderId, amount)
 *   SettlementCompleted(settlementId, orderId)      → SettleOrder(orderId, settlementId)
 * </pre>
 * 각 단계는 command 발행 후 응답 사실을 기다린다 — {@code saga_instance.timeout_at}을 걸어두면, 참여 서비스가
 * 다운/무응답이면 타임아웃 스캐너가 감지한다. 특히 <b>AWAIT_SETTLEMENT</b>(정산 서비스 응답 대기)가 대표 감지 지점.
 * 배송은 물리적으로 되돌릴 수 없어 보상 없음 — 재시도로 수렴. 종결(finish)은 {@link SagaLifecycle}이 오더 종료 사실로 처리.
 */
@Component
public class DeliverSaga {

    private static final Logger log = LoggerFactory.getLogger(DeliverSaga.class);
    private static final String SAGA_TYPE = "order-fulfillment";

    private final EventPublisher events;
    private final SagaStore saga;
    private final long stepTimeoutMs;

    public DeliverSaga(EventPublisher events, SagaStore saga,
                       @Value("${platform.events.saga.step-timeout-ms:30000}") long stepTimeoutMs) {
        this.events = events;
        this.saga = saga;
        this.stepTimeoutMs = stepTimeoutMs;
    }

    @EventHandler
    void onDispatchDelivered(DispatchDelivered e) {
        events.publish(new DeliverOrder(e.orderId(), e.dispatchId()));
        saga.mark(SAGA_TYPE, e.orderId(), corr(e.orderId()), "IN_PROGRESS", "AWAIT_ORDER_DELIVERED", timeoutAt());
        log.info("[deliver-saga {}] 배송완료 → 오더 배송완료 전이 시도(dispatch={})", e.orderId(), e.dispatchId());
    }

    @EventHandler
    void onOrderDelivered(OrderDelivered e) {
        events.publish(new CreateSettlement(e.dispatchId(), e.orderId(), e.amount()));
        // 정산 서비스 응답(SettlementCompleted) 대기 — 정산 다운/유실 시 여기서 타임아웃 감지.
        saga.mark(SAGA_TYPE, e.orderId(), corr(e.orderId()), "IN_PROGRESS", "AWAIT_SETTLEMENT", timeoutAt());
        log.info("[deliver-saga {}] 오더 배송완료 → 정산 생성 지시(amount={})", e.orderId(), e.amount());
    }

    @EventHandler
    void onSettlementCompleted(SettlementCompleted e) {
        events.publish(new SettleOrder(e.orderId(), e.settlementId()));
        saga.mark(SAGA_TYPE, e.orderId(), corr(e.orderId()), "IN_PROGRESS", "AWAIT_ORDER_SETTLED", timeoutAt());
        log.info("[deliver-saga {}] 정산 완료 → 오더 정산완료 전이 시도(settlement={})", e.orderId(), e.settlementId());
    }

    private OffsetDateTime timeoutAt() {
        return OffsetDateTime.now().plusNanos(stepTimeoutMs * 1_000_000L);
    }

    private String corr(String fallback) {
        var c = FlowContext.get();
        return c != null ? c.correlationId() : fallback;
    }
}
