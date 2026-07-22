package com.wemeet.core.event.consume;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventContract;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * BPP(@EventHandler 자동등록)의 엣지·실패 경로 — 잘못된 시그니처 거부, 핸들러 내부 예외 전파 규칙.
 * (정상 등록/디스패치·중복은 {@link EventHandlerRegistrarTest}가 이미 덮는다.)
 */
class EventHandlerRegistrarEdgeTest {

    @EventContract(type = "oms.order.created", version = 1)
    record OrderCreated(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @Test
    void 파라미터가_DomainEvent가_아니면_기동_시점에_실패한다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");

        class BadHandler {
            @EventHandler
            public void on(String notAnEvent) { }
        }

        assertThatThrownBy(() -> registrar.postProcessAfterInitialization(new BadHandler(), "bad"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DomainEvent 파라미터 하나");
    }

    @Test
    void 파라미터가_둘이면_실패한다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");

        class TwoArgHandler {
            @EventHandler
            public void on(OrderCreated e, String extra) { }
        }

        assertThatThrownBy(() -> registrar.postProcessAfterInitialization(new TwoArgHandler(), "two"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 핸들러가_던진_RuntimeException은_그대로_전파된다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");

        class ExplodingHandler {
            @EventHandler
            public void on(OrderCreated e) { throw new IllegalStateException("업무 규칙 위반"); }
        }
        registrar.postProcessAfterInitialization(new ExplodingHandler(), "explode");

        var handler = registry.lookup("oms", "oms.order.created");
        assertThatThrownBy(() -> handler.accept(new OrderCreated("ORD-1")))
                .isInstanceOf(IllegalStateException.class)   // InvocationTargetException 벗겨 원인 RE 그대로
                .hasMessageContaining("업무 규칙 위반");
    }

    @Test
    void 핸들러가_던진_checked_예외는_RuntimeException으로_감싸_전파된다() {
        HandlerRegistry registry = new HandlerRegistry();
        EventHandlerRegistrar registrar = new EventHandlerRegistrar(registry, "oms");

        class CheckedHandler {
            @EventHandler
            public void on(OrderCreated e) throws Exception { throw new Exception("checked"); }
        }
        registrar.postProcessAfterInitialization(new CheckedHandler(), "checked");

        var handler = registry.lookup("oms", "oms.order.created");
        assertThatThrownBy(() -> handler.accept(new OrderCreated("ORD-1")))
                .isInstanceOf(RuntimeException.class);
    }
}
