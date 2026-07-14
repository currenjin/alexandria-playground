package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.event.consume.EventHandler;
import com.wemeet.eventbackbone.common.event.publish.EventPublisher;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.UndispatchOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 배차취소 사가 (#3) — 무상태. 배차 취소 사실 → 오더 미배차 복귀 시도.
 * <pre>DispatchCancelled(dispatchId, orderId) → UndispatchOrder(orderId)</pre>
 * 오더가 실제 DISPATCHED였으면 CREATED로 복귀, 아니면(보상으로 취소된 배차 등) OMS가 무시(가드). 창 없음.
 */
@Component
public class CancelDispatchSaga {

    private static final Logger log = LoggerFactory.getLogger(CancelDispatchSaga.class);
    private final EventPublisher events;

    public CancelDispatchSaga(EventPublisher events) {
        this.events = events;
    }

    @EventHandler
    void onDispatchCancelled(DispatchCancelled e) {
        events.publish(new UndispatchOrder(e.orderId()));
        log.info("[cancel-dispatch-saga {}] 배차취소 → 오더 미배차 복귀 시도(dispatch={})", e.orderId(), e.dispatchId());
    }
}
