package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.saga.SagaStore;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkDelivered;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkDispatched;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkSettled;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkUndispatched;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelRejected;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCreated;
import com.wemeet.eventbackbone.contracts.SettlementContracts.CreateSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementCompleted;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCancelled;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchDelivered;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 주문 이행 프로세스 매니저(중앙 orchestrator, 플랫폼 오너 소유) — 루티프로 미들마일 흐름.
 *
 * <p>참여자(OMS·TMS·BMS)는 이 흐름을 모른다. orchestrator가 서비스 <b>이벤트(사실)</b>를 소비해
 * 다음 <b>커맨드(지시)</b>를 발행하고, 프로세스 상태를 {@link SagaStore}에 추적한다.
 * 매칭 열쇠 = orderId(업무 키) — 배차/배송/취소 액션이 여러 번 나뉘어 들어와도 같은 프로세스에 붙는다.
 *
 * <p>상태: CREATED → DISPATCHED → DELIVERED → SETTLED / CANCELLED. 각 핸들러는 기대 상태 가드로
 * 중복·순서 어긋남·종료 후 도착을 흡수한다(멱등). "배차된 오더 취소 불가" 규칙은 OMS 애그리거트가 판정하며,
 * 여기서는 그 결과 사실(OrderCancelled / OrderCancelRejected)을 관측해 프로세스를 종료/유지한다.
 */
@Component
public class OrderFulfillmentSaga {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfillmentSaga.class);
    private static final String TYPE = "order-fulfillment";

    private final SagaStore store;
    private final EventPublisher events;

    public OrderFulfillmentSaga(SagaStore store, EventPublisher events) {
        this.store = store;
        this.events = events;
    }

    // ── 프로세스 시작 ──
    @EventHandler
    void onOrderCreated(OrderCreated e) {
        if (store.status(e.orderId()) != null) {
            log.info("[process {}] 이미 존재 — 중복 OrderCreated 무시", e.orderId());
            return;
        }
        Map<String, Object> state = new HashMap<>();
        state.put("orderId", e.orderId());
        state.put("amount", e.amount());
        state.put("currency", e.currency());
        store.start(TYPE, e.orderId(), corr(), "CREATED", "created", state, null);
        log.info("[process {}] CREATED", e.orderId());
    }

    // ── 배차 확정 통지 → 오더 상태 동기화 ──
    @EventHandler
    void onDispatchCreated(DispatchCreated e) {
        if (!expect(e.orderId(), "CREATED", "DispatchCreated")) return;
        Map<String, Object> state = store.state(e.orderId());
        state.put("dispatchId", e.dispatchId());
        store.advance(e.orderId(), "DISPATCHED", "dispatched", state, null);
        events.publish(new MarkDispatched(e.orderId(), e.dispatchId()));
        log.info("[process {}] DISPATCHED(dispatch={}) -> MarkDispatched", e.orderId(), e.dispatchId());
    }

    // ── 배송 완료 통지 → 오더 배송완료 + 정산 생성 지시 ──
    @EventHandler
    void onDispatchDelivered(DispatchDelivered e) {
        if (!expect(e.orderId(), "DISPATCHED", "DispatchDelivered")) return;
        Map<String, Object> state = store.state(e.orderId());
        store.advance(e.orderId(), "DELIVERED", "delivered", state, null);
        events.publish(new MarkDelivered(e.orderId()));
        events.publish(new CreateSettlement(e.dispatchId(), e.orderId(), (String) state.get("amount")));
        log.info("[process {}] DELIVERED -> MarkDelivered + CreateSettlement", e.orderId());
    }

    // ── 정산 완료 통지 → 오더 정산완료(종료) ──
    @EventHandler
    void onSettlementCompleted(SettlementCompleted e) {
        if (!expect(e.orderId(), "DELIVERED", "SettlementCompleted")) return;
        store.finish(e.orderId(), "SETTLED");
        events.publish(new MarkSettled(e.orderId(), e.settlementId()));
        log.info("[process {}] SETTLED (settlement={})", e.orderId(), e.settlementId());
    }

    // ── 배차 취소 통지 → 오더 미배차 복귀 ──
    @EventHandler
    void onDispatchCancelled(DispatchCancelled e) {
        if (!expect(e.orderId(), "DISPATCHED", "DispatchCancelled")) return;
        Map<String, Object> state = store.state(e.orderId());
        state.remove("dispatchId");
        store.advance(e.orderId(), "CREATED", "created", state, null);
        events.publish(new MarkUndispatched(e.orderId()));
        log.info("[process {}] 배차취소 -> CREATED(미배차 복귀) -> MarkUndispatched", e.orderId());
    }

    // ── 오더 취소 결과 관측 ──
    @EventHandler
    void onOrderCancelled(OrderCancelled e) {
        if (store.status(e.orderId()) == null) return;
        store.finish(e.orderId(), "CANCELLED");
        log.info("[process {}] CANCELLED (사유={})", e.orderId(), e.reason());
    }

    @EventHandler
    void onOrderCancelRejected(OrderCancelRejected e) {
        // 규칙: 배차된 오더 취소 거부 — 프로세스 상태는 그대로 유지.
        log.info("[process {}] 취소 거부(규칙): 현재 {} — {}", e.orderId(), e.currentStatus(), e.reason());
    }

    private String corr() {
        return FlowContext.get().correlationId();
    }

    /** 기대 상태가 아니면(종료·미존재·순서 어긋남·중복) 조용히 무시 — 멱등·가드. */
    private boolean expect(String orderId, String expected, String evt) {
        String st = store.status(orderId);
        if (expected.equals(st)) return true;
        log.info("[process {}] 상태 {}(기대 {}) — {} 무시", orderId, st, expected, evt);
        return false;
    }
}
