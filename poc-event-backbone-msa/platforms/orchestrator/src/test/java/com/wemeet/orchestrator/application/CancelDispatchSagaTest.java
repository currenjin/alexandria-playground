package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.DispatchCancelled;
import com.wemeet.contract.OrderContracts.UndispatchOrder;
import com.wemeet.core.testsupport.FakeEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 배차취소 사가(#3) — 무상태. 배차 취소 사실 → 오더 미배차 복귀 시도(UndispatchOrder).
 * 오더가 실제 DISPATCHED였으면 CREATED 복귀, 아니면 OMS가 가드로 무시. 보상 창 없음.
 */
class CancelDispatchSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final CancelDispatchSaga saga = new CancelDispatchSaga(events);

    @Test
    void 배차_취소되면_오더_미배차_복귀를_시도한다_UndispatchOrder() {
        saga.onDispatchCancelled(new DispatchCancelled("DISP-1", "ORD-1"));

        assertThat(events.first(UndispatchOrder.class)).isPresent()
                .get().satisfies(cmd -> assertThat(cmd.orderId()).isEqualTo("ORD-1"));
        assertThat(events.count(UndispatchOrder.class)).isEqualTo(1);
    }
}
