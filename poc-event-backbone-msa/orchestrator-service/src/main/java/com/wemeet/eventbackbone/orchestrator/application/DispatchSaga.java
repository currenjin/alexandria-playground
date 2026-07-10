package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.event.consume.EventHandler;
import com.wemeet.eventbackbone.common.event.publish.EventPublisher;
import com.wemeet.eventbackbone.contracts.DispatchContracts.CancelDispatch;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCreated;
import com.wemeet.eventbackbone.contracts.OrderContracts.DispatchOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderDispatchRejected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 배차확정 사가 (#2) — <b>무상태(stateless) 반응</b>. 상관관계는 이벤트가 실어 나른다(상태 저장 불필요).
 * <pre>
 *   DispatchCreated(dispatchId, orderId)        → DispatchOrder(orderId, dispatchId)   // 오더에 배차 전이 시도
 *   OrderDispatchRejected(orderId, dispatchId)  → CancelDispatch(dispatchId, orderId)  // 보상: 배차 되돌리기
 * </pre>
 * 오더 전이의 성패는 <b>OMS 오더 애그리거트</b>가 권위로 판정한다(가드+낙관적 잠금). 거부(취소된 오더 등)면
 * 이 사가가 배차를 보상 취소한다. 성공(OrderDispatched)이면 할 일 없음.
 */
@Component
public class DispatchSaga {

    private static final Logger log = LoggerFactory.getLogger(DispatchSaga.class);
    private final EventPublisher events;

    public DispatchSaga(EventPublisher events) {
        this.events = events;
    }

    @EventHandler
    void onDispatchCreated(DispatchCreated e) {
        events.publish(new DispatchOrder(e.orderId(), e.dispatchId()));
        log.info("[dispatch-saga {}] 배차 확정 → 오더 배차 전이 시도(DispatchOrder, dispatch={})", e.orderId(), e.dispatchId());
    }

    @EventHandler
    void onOrderDispatchRejected(OrderDispatchRejected e) {
        events.publish(new CancelDispatch(e.dispatchId(), e.orderId()));
        log.info("[dispatch-saga {}] 배차 전이 거부(현재 {}) → 보상: 배차취소(dispatch={})",
                e.orderId(), e.currentStatus(), e.dispatchId());
    }
}
