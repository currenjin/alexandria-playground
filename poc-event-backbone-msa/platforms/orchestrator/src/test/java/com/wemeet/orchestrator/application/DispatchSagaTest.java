package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.CancelDispatch;
import com.wemeet.contract.DispatchContracts.DispatchCreated;
import com.wemeet.contract.OrderContracts.DispatchOrder;
import com.wemeet.contract.OrderContracts.OrderDispatchRejected;
import com.wemeet.core.testsupport.FakeEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 배차확정 사가(#2) — 무상태 반응. 상관관계는 이벤트가 실어 나른다.
 * DispatchCreated → DispatchOrder(오더 배차 전이 시도), OrderDispatchRejected → CancelDispatch(보상).
 * 전이 성패는 OMS 오더 애그리거트가 권위로 판정하고, 거부면 이 사가가 배차를 보상 취소한다.
 */
class DispatchSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final DispatchSaga saga = new DispatchSaga(events);

    @Test
    void 배차_확정되면_오더_배차_전이를_시도한다_DispatchOrder() {
        saga.onDispatchCreated(new DispatchCreated("DISP-1", "ORD-1", "CARRIER-1"));

        assertThat(events.first(DispatchOrder.class)).isPresent()
                .get().satisfies(cmd -> {
                    assertThat(cmd.orderId()).isEqualTo("ORD-1");
                    assertThat(cmd.dispatchId()).isEqualTo("DISP-1");
                });
    }

    @Test
    void 오더_배차전이가_거부되면_배차를_보상_취소한다_CancelDispatch() {
        saga.onOrderDispatchRejected(new OrderDispatchRejected("ORD-1", "DISP-1", "CANCELLED"));

        assertThat(events.first(CancelDispatch.class)).isPresent()
                .get().satisfies(cmd -> {
                    assertThat(cmd.dispatchId()).isEqualTo("DISP-1");
                    assertThat(cmd.orderId()).isEqualTo("ORD-1");
                });
    }

    @Test
    void 배차확정_반응은_보상커맨드를_내지_않는다() {
        saga.onDispatchCreated(new DispatchCreated("DISP-2", "ORD-2", "CARRIER-2"));

        assertThat(events.count(CancelDispatch.class)).isZero();
        assertThat(events.count(DispatchOrder.class)).isEqualTo(1);
    }
}
