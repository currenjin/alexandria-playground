package com.wemeet.core.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 실행 컨텍스트(correlationId·causationId·tenant 운반). 봉투를 잇는 것은 record가 아니라 이 ThreadLocal 컨텍스트다:
 * 소비 리스너가 open, 발행자가 read. 진입점 openEntry는 correlationId 전파/생성 규칙을 담는다.
 */
class FlowContextTest {

    @AfterEach
    void tearDown() {
        FlowContext.clear();
    }

    @Test
    void open하면_get으로_같은_컨텍스트를_읽는다() {
        UUID eventId = UuidV7();
        FlowContext.open("dongsuh", "DS-GRP", "corr-1", eventId);

        FlowContext.Ctx ctx = FlowContext.get();

        assertThat(ctx).isNotNull();
        assertThat(ctx.tenantId()).isEqualTo("dongsuh");
        assertThat(ctx.corpId()).isEqualTo("DS-GRP");
        assertThat(ctx.correlationId()).isEqualTo("corr-1");
        assertThat(ctx.currentEventId()).isEqualTo(eventId);
    }

    @Test
    void clear하면_컨텍스트가_사라진다() {
        FlowContext.open("t", "c", "corr", UUID.randomUUID());

        FlowContext.clear();

        assertThat(FlowContext.get()).isNull();
    }

    @Test
    void 설정하지_않으면_get은_null() {
        assertThat(FlowContext.get()).isNull();
    }

    @Test
    void openEntry는_correlationId가_있으면_그대로_전파한다() {
        FlowContext.openEntry("t", "c", "given-corr");

        FlowContext.Ctx ctx = FlowContext.get();
        assertThat(ctx.correlationId()).isEqualTo("given-corr");
        assertThat(ctx.currentEventId()).isNull();   // 최초 진입 — causation 없음
    }

    @Test
    void openEntry는_correlationId가_없으면_새로_생성한다() {
        FlowContext.openEntry("t", "c", null);

        FlowContext.Ctx ctx = FlowContext.get();
        assertThat(ctx.correlationId()).isNotNull();
        // 생성된 값은 UUID 형식
        assertThat(UUID.fromString(ctx.correlationId())).isNotNull();
        assertThat(ctx.currentEventId()).isNull();
    }

    private static UUID UuidV7() {
        return UUID.randomUUID();
    }
}
