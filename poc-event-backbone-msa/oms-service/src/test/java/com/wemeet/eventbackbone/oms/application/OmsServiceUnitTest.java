package com.wemeet.eventbackbone.oms.application;

import com.wemeet.eventbackbone.common.event.HandlerRegistry;
import com.wemeet.eventbackbone.contracts.OrderContracts.CancelOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderConfirmed;
import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.support.FakeEventPublisher;
import com.wemeet.eventbackbone.oms.support.InMemoryOrderRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * §7.1.8 테스트 1층 — FakeEventPublisher로 비즈 로직만 검증(인프라 0).
 * 확정한 규칙: 도메인 변경과 이벤트 발행이 한 유스케이스 안에서 함께 일어난다.
 */
class OmsServiceUnitTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final InMemoryOrderRepository orders = new InMemoryOrderRepository();
    private final OmsService oms = new OmsService(orders, events, new HandlerRegistry());

    @Test
    void confirm_saves_order_and_publishes_OrderConfirmed() {
        oms.confirm("ORD-1", "CUST-1", "1250000", "KRW");

        assertThat(orders.find("ORD-1").status()).isEqualTo(Order.CONFIRMED);
        assertThat(events.first(OrderConfirmed.class)).isPresent()
                .get().satisfies(e -> {
                    assertThat(e.orderId()).isEqualTo("ORD-1");
                    assertThat(e.amount()).isEqualTo("1250000");
                    assertThat(e.currency()).isEqualTo("KRW");
                });
    }

    @Test
    void onCancelOrder_cancels_order_and_publishes_OrderCancelled() {
        oms.confirm("ORD-2", "CUST-2", "1000", "KRW");
        events.published.clear();

        oms.onCancelOrder(new CancelOrder("ORD-2", "배차 실패"));

        assertThat(orders.find("ORD-2").status()).isEqualTo(Order.CANCELLED);
        assertThat(events.first(OrderCancelled.class)).isPresent()
                .get().satisfies(e -> assertThat(e.reason()).isEqualTo("배차 실패"));
    }
}
