package com.wemeet.eventbackbone.oms.application;

import com.wemeet.eventbackbone.contracts.OrderContracts.MarkDispatched;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelRejected;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCreated;
import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.support.FakeEventPublisher;
import com.wemeet.eventbackbone.oms.support.InMemoryOrderRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * §7.1.8 테스트 1층 — FakeEventPublisher로 비즈 로직만 검증(인프라 0).
 * 규칙: 도메인 변경과 이벤트 발행이 한 유스케이스에서 함께 일어난다. "배차된 오더 취소 불가"는 여기서 판정.
 */
class OmsServiceUnitTest {

    private final FakeEventPublisher events = new FakeEventPublisher();
    private final InMemoryOrderRepository orders = new InMemoryOrderRepository();
    private final OmsService oms = new OmsService(orders, events);

    @Test
    void create_saves_order_and_publishes_OrderCreated() {
        oms.create("ORD-1", "SHIPPER-1", "서울", "부산", "1250000", "KRW");

        assertThat(orders.find("ORD-1").status()).isEqualTo(Order.CREATED);
        assertThat(events.first(OrderCreated.class)).isPresent()
                .get().satisfies(e -> {
                    assertThat(e.orderId()).isEqualTo("ORD-1");
                    assertThat(e.origin()).isEqualTo("서울");
                    assertThat(e.destination()).isEqualTo("부산");
                    assertThat(e.amount()).isEqualTo("1250000");
                });
    }

    @Test
    void cancel_when_created_cancels_and_publishes_OrderCancelled() {
        oms.create("ORD-2", "SHIPPER-1", "서울", "부산", "1000", "KRW");
        events.published.clear();

        oms.cancelOrder("ORD-2", "고객 취소");

        assertThat(orders.find("ORD-2").status()).isEqualTo(Order.CANCELLED);
        assertThat(events.first(OrderCancelled.class)).isPresent()
                .get().satisfies(e -> assertThat(e.reason()).isEqualTo("고객 취소"));
    }

    @Test
    void cancel_when_dispatched_is_rejected_by_rule() {
        oms.create("ORD-3", "SHIPPER-1", "서울", "부산", "1000", "KRW");
        oms.onMarkDispatched(new MarkDispatched("ORD-3", "TRIP-1"));   // 배차됨
        events.published.clear();

        oms.cancelOrder("ORD-3", "고객 취소");

        assertThat(orders.find("ORD-3").status()).isEqualTo(Order.DISPATCHED);   // 취소 안 됨
        assertThat(events.first(OrderCancelRejected.class)).isPresent()
                .get().satisfies(e -> assertThat(e.currentStatus()).isEqualTo(Order.DISPATCHED));
    }
}
