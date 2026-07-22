package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.CancelDispatch;
import com.wemeet.contract.DispatchContracts.DispatchCreated;
import com.wemeet.contract.OrderContracts.DispatchOrder;
import com.wemeet.contract.OrderContracts.OrderDispatchRejected;
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
 * 배차확정 사가(#2) — 무상태 반응 + 진행 기록(saga_instance). 상관관계는 이벤트가 실어 나른다.
 * DispatchCreated → DispatchOrder(오더 배차 전이 시도), OrderDispatchRejected → CancelDispatch(보상).
 */
class DispatchSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final SagaStore store = mock(SagaStore.class);
    private final DispatchSaga saga = new DispatchSaga(events, store);

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
    void 배차_확정시_프로세스_진행을_기록한다_mark() {
        saga.onDispatchCreated(new DispatchCreated("DISP-1", "ORD-1", "CARRIER-1"));

        // 크로스서비스 프로세스 시작 기록. 배차~배송은 사용자 액션 대기라 timeout 없음(null).
        verify(store).mark(eq("order-fulfillment"), eq("ORD-1"), anyString(),
                eq("IN_PROGRESS"), eq("DISPATCH_REQUESTED"), isNull());
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
