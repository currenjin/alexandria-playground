package com.wemeet.orchestrator.application;

import com.wemeet.core.context.FlowContext;
import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.event.publish.EventPublisher;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.contract.DispatchContracts.DispatchCancelled;
import com.wemeet.contract.OrderContracts.UndispatchOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 배차취소 사가 (#3) — 무상태 반응 + 진행 기록. 배차 취소 사실 → 오더 미배차 복귀 시도.
 * <pre>DispatchCancelled(dispatchId, orderId) → UndispatchOrder(orderId)</pre>
 * 오더가 실제 DISPATCHED였으면 CREATED로 복귀, 아니면(보상으로 취소된 배차 등) OMS가 무시(가드). 창 없음.
 * 종결(finish)은 {@link SagaLifecycle}이 OrderUndispatched 사실로 처리.
 */
@Component
public class CancelDispatchSaga {

    private static final Logger log = LoggerFactory.getLogger(CancelDispatchSaga.class);
    private static final String SAGA_TYPE = "order-fulfillment";

    private final EventPublisher events;
    private final SagaStore saga;

    public CancelDispatchSaga(EventPublisher events, SagaStore saga) {
        this.events = events;
        this.saga = saga;
    }

    @EventHandler
    void onDispatchCancelled(DispatchCancelled e) {
        saga.mark(SAGA_TYPE, e.orderId(), corr(e.orderId()), "CANCELLING", "AWAIT_UNDISPATCH", null);
        events.publish(new UndispatchOrder(e.orderId()));
        log.info("[cancel-dispatch-saga {}] 배차취소 → 오더 미배차 복귀 시도(dispatch={})", e.orderId(), e.dispatchId());
    }

    private String corr(String fallback) {
        var c = FlowContext.get();
        return c != null ? c.correlationId() : fallback;
    }
}
