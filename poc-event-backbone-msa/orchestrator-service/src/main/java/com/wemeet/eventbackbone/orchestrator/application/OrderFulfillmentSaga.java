package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.saga.SagaStore;
import com.wemeet.eventbackbone.contracts.OrderContracts.CancelOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderConfirmed;
import com.wemeet.eventbackbone.contracts.SettlementContracts.ScheduleSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementScheduled;
import com.wemeet.eventbackbone.contracts.TripContracts.CancelTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.CreateTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.TripCreationFailed;
import com.wemeet.eventbackbone.contracts.TripContracts.TripDispatched;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

/**
 * 주문 이행 사가(flow). 플랫폼 오너가 소유하고 중앙 orchestrator에 산다 — 참여자(OMS·TMS·BMS)는 이 흐름을 모른다.
 * order.confirmed → CreateTrip → trip.dispatched → ScheduleSettlement → settlement.scheduled(완료),
 * 배차 실패 시 CancelOrder로 보상. 매칭 열쇠는 correlationId. 사가 엔진(SagaStore)은 platform-core 공통.
 */
@Component
public class OrderFulfillmentSaga {

    private static final Logger log = LoggerFactory.getLogger(OrderFulfillmentSaga.class);
    private static final String TYPE = "order-fulfillment";
    private static final Set<String> ACTIVE = Set.of("STARTED", "DISPATCHED");

    private final SagaStore store;
    private final EventPublisher events;
    private final long stepTimeoutMs;
    private final int maxStepRetries;

    public OrderFulfillmentSaga(SagaStore store, EventPublisher events,
                                @Value("${platform.events.saga.step-timeout-ms:30000}") long stepTimeoutMs,
                                @Value("${platform.events.saga.max-step-retries:2}") int maxStepRetries) {
        this.store = store;
        this.events = events;
        this.stepTimeoutMs = stepTimeoutMs;
        this.maxStepRetries = maxStepRetries;
    }

    @EventHandler
    void onOrderConfirmed(OrderConfirmed e) {
        if (store.status(corr()) != null) {
            log.info("[saga {}] 이미 시작됨 — 중복 OrderConfirmed 무시", corr());
            return;
        }
        Map<String, Object> state = new HashMap<>(Map.of(
                "orderId", e.orderId(), "amount", e.amount(), "currency", e.currency()));
        store.start(TYPE, e.orderId(), corr(), state, deadline());
        events.publish(new CreateTrip(e.orderId(), e.amount(), e.currency()));
        log.info("[saga {}] STARTED -> CreateTrip({})", corr(), e.orderId());
    }

    @EventHandler
    void onTripDispatched(TripDispatched e) {
        String st = store.status(corr());
        if (!ACTIVE.contains(st)) {
            if ("COMPENSATING".equals(st) || "COMPENSATED".equals(st)) {
                events.publish(new CancelTrip(e.tripId()));
                log.info("[saga {}] 보상된 흐름에 늦은 TripDispatched → CancelTrip({}) (orphan 보상)", corr(), e.tripId());
            } else {
                log.info("[saga {}] 활성 아님({}) — TripDispatched 무시", corr(), st);
            }
            return;
        }
        Map<String, Object> state = store.state(corr());
        state.put("tripId", e.tripId());
        store.advance(corr(), "DISPATCHED", "settlement", state, deadline());
        events.publish(new ScheduleSettlement(e.tripId(), (String) state.get("amount")));
        log.info("[saga {}] DISPATCHED(trip={}) -> ScheduleSettlement", corr(), e.tripId());
    }

    @EventHandler
    void onSettlementScheduled(SettlementScheduled e) {
        if (!active()) { log.info("[saga {}] 활성 아님 — SettlementScheduled 무시", corr()); return; }
        store.finish(corr(), "COMPLETED");
        log.info("[saga {}] COMPLETED (settlement={})", corr(), e.settlementId());
    }

    @EventHandler
    void onTripCreationFailed(TripCreationFailed e) {
        if (!active()) { log.info("[saga {}] 활성 아님 — 보상 중복 무시", corr()); return; }
        store.advance(corr(), "COMPENSATING", "compensate", store.state(corr()), null);
        events.publish(new CancelOrder(e.orderId(), "배차 실패: " + e.reason()));
        store.finish(corr(), "COMPENSATED");
        log.info("[saga {}] COMPENSATING -> CancelOrder -> COMPENSATED", corr());
    }

    /** step 무응답(타임아웃) 감지 → 보상. 타임아웃은 실패가 아니므로 실무에선 재시도가 선행한다. */
    @Scheduled(fixedDelayString = "${platform.events.saga.timeout-scan-ms:1000}")
    @Transactional
    public void scanTimeouts() {
        for (SagaStore.TimedOut s : store.findTimedOut()) {
            FlowContext.open(null, null, s.correlationId(), null);
            try {
                Map<String, Object> state = store.state(s.correlationId());
                int retries = state.get("retries") == null ? 0 : ((Number) state.get("retries")).intValue();
                if (retries < maxStepRetries) {
                    state.put("retries", retries + 1);
                    resendStepCommand(s.currentStep(), state);
                    store.advance(s.correlationId(), s.status(), s.currentStep(), state, deadline());
                    log.warn("[saga {}] 타임아웃 -> step 재시도 {}/{} (step={})",
                            s.correlationId(), retries + 1, maxStepRetries, s.currentStep());
                } else {
                    store.advance(s.correlationId(), "COMPENSATING", "compensate", state, null);
                    events.publish(new CancelOrder(s.aggregateId(), "step 타임아웃 (재시도 소진)"));
                    if (state.get("tripId") != null) {
                        events.publish(new CancelTrip((String) state.get("tripId")));
                    }
                    store.finish(s.correlationId(), "COMPENSATED");
                    log.warn("[saga {}] 타임아웃 재시도 소진 -> 보상", s.correlationId());
                }
            } finally {
                FlowContext.clear();
            }
        }
    }

    private String corr() {
        return FlowContext.get().correlationId();
    }

    /** 진행 중(STARTED/DISPATCHED)인가 — 종료·미존재 사가면 false. */
    private boolean active() {
        return ACTIVE.contains(store.status(corr()));
    }

    /** 타임아웃 재시도 — 현재 step에 해당하는 커맨드를 재전송. 참여자 step은 aggregateId 멱등이라 중복 생성 없음. */
    private void resendStepCommand(String step, Map<String, Object> state) {
        if ("dispatch".equals(step)) {
            events.publish(new CreateTrip((String) state.get("orderId"),
                    (String) state.get("amount"), (String) state.get("currency")));
        } else if ("settlement".equals(step)) {
            events.publish(new ScheduleSettlement((String) state.get("tripId"), (String) state.get("amount")));
        }
    }

    private OffsetDateTime deadline() {
        return OffsetDateTime.now(ZoneOffset.UTC).plusNanos(stepTimeoutMs * 1_000_000);
    }
}
