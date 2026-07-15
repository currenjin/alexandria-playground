package com.wemeet.core.event.contract;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventTypesTest {

    @EventContract(type = "oms.order.created", version = 2)
    record OrderCreated(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    @Test
    void 토픽은_eventType_앞_두_마디() {
        assertThat(EventTypes.topicOf("oms.order.created")).isEqualTo("oms.order");
        assertThat(EventTypes.topicOf("tms.dispatch.delivered")).isEqualTo("tms.dispatch");
        assertThat(EventTypes.topicOf("oms.cmd.dispatch_order")).isEqualTo("oms.cmd");
    }

    @Test
    void 한_마디면_그대로() {
        assertThat(EventTypes.topicOf("ping")).isEqualTo("ping");
    }

    @Test
    void register하면_type와_class가_양방향_매핑된다() {
        EventTypes.register(OrderCreated.class);
        assertThat(EventTypes.typeOf(OrderCreated.class)).isEqualTo("oms.order.created");
        assertThat(EventTypes.classOf("oms.order.created")).isEqualTo(OrderCreated.class);
        assertThat(EventTypes.versionOf(OrderCreated.class)).isEqualTo(2);
    }

    @Test
    void 미등록_타입_조회는_예외() {
        assertThatThrownBy(() -> EventTypes.classOf("no.such.type"))
                .isInstanceOf(IllegalStateException.class);
    }
}
