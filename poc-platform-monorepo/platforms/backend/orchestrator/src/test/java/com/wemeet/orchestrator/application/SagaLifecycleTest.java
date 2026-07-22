package com.wemeet.orchestrator.application;

import com.wemeet.contract.OrderContracts.OrderCancelled;
import com.wemeet.contract.OrderContracts.OrderSettled;
import com.wemeet.contract.OrderContracts.OrderUndispatched;
import com.wemeet.core.saga.SagaStore;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * 사가 생애 종결 — 오더 종료 사실을 받아 saga_instance를 finish로 마감(timeout_at 해제 → 스캐너 오탐 방지).
 */
class SagaLifecycleTest {

    private final SagaStore store = mock(SagaStore.class);
    private final SagaLifecycle lifecycle = new SagaLifecycle(store);

    @Test
    void 정산완료_사실이면_프로세스를_SETTLED로_종결한다() {
        lifecycle.onOrderSettled(new OrderSettled("ORD-1", "SET-1"));
        verify(store).finish("ORD-1", "SETTLED");
    }

    @Test
    void 취소_사실이면_CANCELLED로_종결한다() {
        lifecycle.onOrderCancelled(new OrderCancelled("ORD-1", "사용자 취소"));
        verify(store).finish("ORD-1", "CANCELLED");
    }

    @Test
    void 미배차_복귀_사실이면_UNDISPATCHED로_종결한다() {
        lifecycle.onOrderUndispatched(new OrderUndispatched("ORD-1"));
        verify(store).finish("ORD-1", "UNDISPATCHED");
    }
}
