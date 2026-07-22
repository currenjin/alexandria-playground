package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.DispatchCancelled;
import com.wemeet.contract.OrderContracts.UndispatchOrder;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.core.testsupport.FakeEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * 배차취소 사가(#3) — 무상태 반응 + 진행 기록. 배차 취소 사실 → 오더 미배차 복귀 시도(UndispatchOrder).
 */
class CancelDispatchSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final SagaStore store = mock(SagaStore.class);
    private final CancelDispatchSaga saga = new CancelDispatchSaga(events, store);

    @Test
    void 배차_취소되면_오더_미배차_복귀를_시도한다_UndispatchOrder() {
        saga.onDispatchCancelled(new DispatchCancelled("DISP-1", "ORD-1"));

        assertThat(events.first(UndispatchOrder.class)).isPresent()
                .get().satisfies(cmd -> assertThat(cmd.orderId()).isEqualTo("ORD-1"));
        assertThat(events.count(UndispatchOrder.class)).isEqualTo(1);
    }

    @Test
    void 배차_취소시_취소_진행을_기록한다_mark() {
        saga.onDispatchCancelled(new DispatchCancelled("DISP-1", "ORD-1"));

        verify(store).mark(eq("order-fulfillment"), eq("ORD-1"), anyString(),
                eq("CANCELLING"), eq("AWAIT_UNDISPATCH"), isNull());
    }
}
