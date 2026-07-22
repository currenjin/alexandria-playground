package com.wemeet.orchestrator.application;

import com.wemeet.core.context.FlowContext;
import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.event.publish.EventPublisher;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.contract.DispatchContracts.CancelDispatch;
import com.wemeet.contract.DispatchContracts.DispatchCreated;
import com.wemeet.contract.OrderContracts.DispatchOrder;
import com.wemeet.contract.OrderContracts.OrderDispatchRejected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 배차확정 사가 (#2) — <b>무상태 반응 + 진행 기록(saga_instance)</b>. 상관관계는 이벤트가 실어 나른다.
 * <pre>
 *   DispatchCreated(dispatchId, orderId)        → DispatchOrder(orderId, dispatchId)   // 오더에 배차 전이 시도
 *   OrderDispatchRejected(orderId, dispatchId)  → CancelDispatch(dispatchId, orderId)  // 보상: 배차 되돌리기
 * </pre>
 * 오더 전이의 성패는 <b>OMS 오더 애그리거트</b>가 권위로 판정한다(가드+낙관적 잠금). 거부면 이 사가가 배차를 보상 취소.
 * 진행 기록은 {@link SagaStore}에 남긴다(liveness 감지용) — 반응 로직은 여전히 무상태.
 */
@Component
public class DispatchSaga {

    private static final Logger log = LoggerFactory.getLogger(DispatchSaga.class);
    private static final String SAGA_TYPE = "order-fulfillment";

    private final EventPublisher events;
    private final SagaStore saga;

    public DispatchSaga(EventPublisher events, SagaStore saga) {
        this.events = events;
        this.saga = saga;
    }

    @EventHandler
    void onDispatchCreated(DispatchCreated e) {
        // 크로스서비스 프로세스 시작(재배차면 진행 기록 갱신). 배차~배송은 사용자 액션 대기라 timeout 없음(null).
        saga.mark(SAGA_TYPE, e.orderId(), corr(e.orderId()), "IN_PROGRESS", "DISPATCH_REQUESTED", null);
        events.publish(new DispatchOrder(e.orderId(), e.dispatchId()));
        log.info("[dispatch-saga {}] 배차 확정 → 오더 배차 전이 시도(DispatchOrder, dispatch={})", e.orderId(), e.dispatchId());
    }

    @EventHandler
    void onOrderDispatchRejected(OrderDispatchRejected e) {
        events.publish(new CancelDispatch(e.dispatchId(), e.orderId()));
        log.info("[dispatch-saga {}] 배차 전이 거부(현재 {}) → 보상: 배차취소(dispatch={})",
                e.orderId(), e.currentStatus(), e.dispatchId());
    }

    private String corr(String fallback) {
        var c = FlowContext.get();
        return c != null ? c.correlationId() : fallback;
    }
}
