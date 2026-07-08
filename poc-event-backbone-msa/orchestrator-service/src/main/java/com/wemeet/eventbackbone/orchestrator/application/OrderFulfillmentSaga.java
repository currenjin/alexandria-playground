package com.wemeet.eventbackbone.orchestrator.application;

import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.saga.SagaStore;
import com.wemeet.eventbackbone.contracts.OrderContracts.CancelOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderConfirmed;
import com.wemeet.eventbackbone.contracts.SettlementContracts.ScheduleSettlement;
import com.wemeet.eventbackbone.contracts.SettlementContracts.SettlementScheduled;
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

    private final SagaStore store;
    private final EventPublisher events;
    private final long stepTimeoutMs;

    public OrderFulfillmentSaga(SagaStore store, EventPublisher events,
                                @Value("${platform.events.saga.step-timeout-ms:30000}") long stepTimeoutMs) {
        this.store = store;
        this.events = events;
        this.stepTimeoutMs = stepTimeoutMs;
    }

    @EventHandler
    void onOrderConfirmed(OrderConfirmed e) {
        Map<String, Object> state = new HashMap<>(Map.of(
                "orderId", e.orderId(), "amount", e.amount(), "currency", e.currency()));
        store.start(TYPE, e.orderId(), corr(), state, deadline());
        events.publish(new CreateTrip(e.orderId(), e.amount(), e.currency()));
        log.info("[saga {}] STARTED -> CreateTrip({})", corr(), e.orderId());
    }

    @EventHandler
    void onTripDispatched(TripDispatched e) {
        Map<String, Object> state = store.state(corr());
        state.put("tripId", e.tripId());
        store.advance(corr(), "DISPATCHED", "settlement", state, deadline());
        events.publish(new ScheduleSettlement(e.tripId(), (String) state.get("amount")));
        log.info("[saga {}] DISPATCHED(trip={}) -> ScheduleSettlement", corr(), e.tripId());
    }

    @EventHandler
    void onSettlementScheduled(SettlementScheduled e) {
        store.finish(corr(), "COMPLETED");
        log.info("[saga {}] COMPLETED (settlement={})", corr(), e.settlementId());
    }

    @EventHandler
    void onTripCreationFailed(TripCreationFailed e) {
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
                store.advance(s.correlationId(), "COMPENSATING", "compensate", store.state(s.correlationId()), null);
                events.publish(new CancelOrder(s.aggregateId(), "step 타임아웃"));
                store.finish(s.correlationId(), "COMPENSATED");
                log.warn("[saga {}] 타임아웃 -> 보상", s.correlationId());
            } finally {
                FlowContext.clear();
            }
        }
    }

    private String corr() {
        return FlowContext.get().correlationId();
    }

    private OffsetDateTime deadline() {
        return OffsetDateTime.now(ZoneOffset.UTC).plusNanos(stepTimeoutMs * 1_000_000);
    }
}
