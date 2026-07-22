package com.wemeet.core.event.consume;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventContract;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventHandlerRegistrarTest {

    @EventContract(type = "oms.order.created", version = 1)
    record OrderCreated(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    /** 비즈 개발자 코드 — @EventHandler만, 이벤트 타입은 파라미터에서 유도(속성 없음). */
    static class OrderConsumer {
        final AtomicReference<String> handled = new AtomicReference<>();
        @EventHandler
        public void on(OrderCreated e) { handled.set(e.orderId()); }
    }

    @Test
    void BPP가_EventHandler를_그룹_이벤트타입_키로_등록하고_디스패치한다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");
        OrderConsumer bean = new OrderConsumer();

        registrar.postProcessAfterInitialization(bean, "orderConsumer");

        var handler = registry.lookup("oms", "oms.order.created"); // 타입은 파라미터 @EventContract에서
        assertThat(handler).isNotNull();

        handler.accept(new OrderCreated("ORD-1"));
        assertThat(bean.handled.get()).isEqualTo("ORD-1");
    }

    @Test
    void 다른_컨슈머_그룹으로는_조회되지_않는다() {
        HandlerRegistry registry = new HandlerRegistry();
        new EventHandlerRegistrar(registry, "oms").postProcessAfterInitialization(new OrderConsumer(), "c");
        assertThat(registry.lookup("tms", "oms.order.created")).isNull();
    }

    @Test
    void 같은_그룹에_같은_이벤트_핸들러가_둘이면_기동_시점에_실패한다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");
        registrar.postProcessAfterInitialization(new OrderConsumer(), "first");

        // 조용한 덮어쓰기(먼저 등록된 핸들러 무경고 소실) 대신 fail-fast
        assertThatThrownBy(() -> registrar.postProcessAfterInitialization(new OrderConsumer(), "second"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복 @EventHandler");
    }
}
