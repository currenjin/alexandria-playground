package com.wemeet.orchestrator.application;

import com.wemeet.contract.DispatchContracts.DispatchDelivered;
import com.wemeet.contract.OrderContracts.DeliverOrder;
import com.wemeet.contract.OrderContracts.OrderDelivered;
import com.wemeet.contract.OrderContracts.SettleOrder;
import com.wemeet.contract.SettlementContracts.CreateSettlement;
import com.wemeet.contract.SettlementContracts.SettlementCompleted;
import com.wemeet.core.testsupport.FakeEventPublisher;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 배송완료 사가(#5) — 무상태 3단. 배송 사실 → 오더 배송완료 → 정산 생성 → 오더 정산완료.
 * 배송은 되돌릴 수 없어 보상 없음(각 단계는 재시도로 수렴). amount는 권위(오더)가 실어준 값을 그대로 전달한다.
 */
class DeliverSagaTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final DeliverSaga saga = new DeliverSaga(events);

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
                    assertThat(cmd.amount()).isEqualTo("1250000");   // 오더가 실어준 금액 그대로
                });
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

        // 세 반응 각각 정확히 하나의 전진 커맨드만 — 보상/롤백 없음
        assertThat(events.count(DeliverOrder.class)).isEqualTo(1);
        assertThat(events.count(CreateSettlement.class)).isEqualTo(1);
        assertThat(events.count(SettleOrder.class)).isEqualTo(1);
        assertThat(events.published).hasSize(3);
    }
}
