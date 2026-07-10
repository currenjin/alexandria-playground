package com.wemeet.eventbackbone.oms.contract;

import com.wemeet.eventbackbone.common.event.EventTypes;
import com.wemeet.eventbackbone.contracts.ContractCatalog;
import com.wemeet.eventbackbone.common.event.DomainEvent;
import com.wemeet.eventbackbone.common.event.EventContract;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * §7.1.8 테스트 3층 — 계약 테스트. 모든 이벤트/커맨드가 @EventContract를 갖고,
 * 논리 타입명이 유일하며, 토픽 유도(§7.1.2 앞 두 마디)가 규칙대로인지 강제.
 * (실무의 CI 게이트를 예제 테스트로 축소.)
 */
class ContractCatalogTest {

    @Test
    void every_contract_is_annotated_and_registers_both_ways() {
        for (Class<? extends DomainEvent> c : ContractCatalog.ALL) {
            EventContract ann = c.getAnnotation(EventContract.class);
            assertThat(ann).as("@EventContract on %s", c.getName()).isNotNull();
            assertThat(ann.type()).as("type() of %s", c.getName()).isNotBlank();
            assertThat(ann.version()).isPositive();

            EventTypes.register(c);
            assertThat(EventTypes.classOf(ann.type())).isEqualTo(c);
            assertThat(EventTypes.typeOf(c)).isEqualTo(ann.type());
        }
    }

    @Test
    void logical_types_are_unique() {
        long distinct = ContractCatalog.ALL.stream()
                .map(c -> c.getAnnotation(EventContract.class).type())
                .distinct().count();
        assertThat(distinct).isEqualTo(ContractCatalog.ALL.size());
    }

    @Test
    void topic_is_first_two_segments_of_type() {
        assertThat(EventTypes.topicOf("oms.order.created")).isEqualTo("oms.order");
        assertThat(EventTypes.topicOf("tms.dispatch.created")).isEqualTo("tms.dispatch");
        assertThat(EventTypes.topicOf("bms.settlement.completed")).isEqualTo("bms.settlement");
        assertThat(EventTypes.topicOf("oms.cmd.dispatch_order")).isEqualTo("oms.cmd");
    }
}
