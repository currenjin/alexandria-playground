package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.DispatchDelivered;
import com.wemeet.contract.OrderContracts.DeliverOrder;
import com.wemeet.contract.OrderContracts.OrderDelivered;
import com.wemeet.contract.OrderContracts.SettleOrder;
import com.wemeet.contract.SettlementContracts.CreateSettlement;
import com.wemeet.contract.SettlementContracts.SettlementCompleted;
import com.wemeet.core.saga.SagaStore;
import com.wemeet.core.testsupport.FakeEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * 배송완료 사가(#5) — 무상태 3단 + 진행 기록. 배송 사실 → 오더 배송완료 → 정산 생성 → 오더 정산완료.
 * 각 단계는 command 발행 후 응답 사실을 기다리며 saga_instance에 timeout을 건다(특히 정산 대기 = 다운 감지 지점).
 */
class DeliverSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final SagaStore store = mock(SagaStore.class);
    private final DeliverSaga saga = new DeliverSaga(events, store, 30000L);

    @Test
    void 배송완료_사실이면_오더_배송완료_전이를_시도한다_DeliverOrder() {
        saga.onDispatchDelivered(new DispatchDelivered("DISP-1", "ORD-1"));

        assertThat(events.first(DeliverOrder.class)).isPresent()
                .get().satisfies(cmd -> {
                    assertThat(cmd.orderId()).isEqualTo("ORD-1");
                    assertThat(cmd.dispatchId()).isEqualTo("DISP-1");
                });
    }

    @Test
    void 오더_배송완료면_권위가_실어준_amount로_정산_생성을_지시한다_CreateSettlement() {
        saga.onOrderDelivered(new OrderDelivered("ORD-1", "DISP-1", "1250000"));

        assertThat(events.first(CreateSettlement.class)).isPresent()
                .get().satisfies(cmd -> {
                    assertThat(cmd.dispatchId()).isEqualTo("DISP-1");
                    assertThat(cmd.orderId()).isEqualTo("ORD-1");
                    assertThat(cmd.amount()).isEqualTo("1250000");
                });
    }

    @Test
    void 오더_배송완료시_정산_대기_step에_타임아웃을_건다_mark() {
        saga.onOrderDelivered(new OrderDelivered("ORD-1", "DISP-1", "1250000"));

        // 정산 서비스 응답(SettlementCompleted) 대기 — 정산 다운/유실 시 여기서 타임아웃 감지된다.
        verify(store).mark(eq("order-fulfillment"), eq("ORD-1"), anyString(),
                eq("IN_PROGRESS"), eq("AWAIT_SETTLEMENT"), notNull());
    }

    @Test
    void 정산_완료되면_오더_정산완료_전이를_시도한다_SettleOrder() {
        saga.onSettlementCompleted(new SettlementCompleted("SET-1", "DISP-1", "ORD-1", "1250000"));

        assertThat(events.first(SettleOrder.class)).isPresent()
                .get().satisfies(cmd -> {
                    assertThat(cmd.orderId()).isEqualTo("ORD-1");
                    assertThat(cmd.settlementId()).isEqualTo("SET-1");
                });
    }

    @Test
    void 배송완료_사가는_어느_단계에서도_보상커맨드를_내지_않는다() {
        saga.onDispatchDelivered(new DispatchDelivered("DISP-1", "ORD-1"));
        saga.onOrderDelivered(new OrderDelivered("ORD-1", "DISP-1", "1000"));
        saga.onSettlementCompleted(new SettlementCompleted("SET-1", "DISP-1", "ORD-1", "1000"));

        assertThat(events.count(DeliverOrder.class)).isEqualTo(1);
        assertThat(events.count(CreateSettlement.class)).isEqualTo(1);
        assertThat(events.count(SettleOrder.class)).isEqualTo(1);
        assertThat(events.published).hasSize(3);
    }
}
