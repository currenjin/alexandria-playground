package com.wemeet.core.event.consume;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.core.context.FlowContext;
import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.Envelope;
import com.wemeet.core.event.contract.EventContract;
import com.wemeet.core.event.contract.EventJson;
import com.wemeet.core.inbox.InboxRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 공통 소비 파이프라인: envelope 파싱 → inbox 멱등 → 컨텍스트 복원 → 핸들러 디스패치.
 * 핸들러 실패는 롤백돼 재전달 때 다시 처리, 역직렬화 실패는 재시도 없이 DLT(NonRetryable).
 */
class EventConsumerSupportTest {

    @EventContract(type = "oms.order.created", version = 1)
    record OrderCreated(String orderId) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    private final ObjectMapper mapper = EventJson.mapper();
    private final InboxRepository inbox = mock(InboxRepository.class);
    private final HandlerRegistry registry = new HandlerRegistry();
    private final EventConsumerSupport support = new EventConsumerSupport(inbox, registry);

    @AfterEach
    void tearDown() {
        FlowContext.clear();
    }

    private String envelope(UUID eventId, String type, OrderCreated payload) throws Exception {
        Envelope env = new Envelope(eventId, type, 1,
                OffsetDateTime.now(ZoneOffset.UTC), payload.orderId(),
                "dongsuh", "DS-GRP", "corr-1", null, mapper.valueToTree(payload));
        return mapper.writeValueAsString(env);
    }

    @Test
    void 정상_경로는_컨텍스트를_복원하고_핸들러로_디스패치한다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(eq("oms"), eq(eventId))).thenReturn(true);
        AtomicReference<String> handledOrder = new AtomicReference<>();
        AtomicReference<FlowContext.Ctx> ctxAtHandle = new AtomicReference<>();
        registry.register("oms", OrderCreated.class, e -> {
            handledOrder.set(e.orderId());
            ctxAtHandle.set(FlowContext.get());   // 핸들러 실행 중엔 봉투에서 복원된 컨텍스트가 열려 있어야 한다
        });

        support.consume("oms", envelope(eventId, "oms.order.created", new OrderCreated("ORD-1")));

        assertThat(handledOrder.get()).isEqualTo("ORD-1");
        assertThat(ctxAtHandle.get().tenantId()).isEqualTo("dongsuh");
        assertThat(ctxAtHandle.get().correlationId()).isEqualTo("corr-1");
        assertThat(ctxAtHandle.get().currentEventId()).isEqualTo(eventId);
        assertThat(FlowContext.get()).isNull();   // finally에서 clear
    }

    @Test
    void 중복_이벤트는_핸들러를_타지_않고_skip한다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(eq("oms"), eq(eventId))).thenReturn(false);   // 이미 처리됨
        AtomicReference<String> handled = new AtomicReference<>();
        registry.register("oms", OrderCreated.class, e -> handled.set(e.orderId()));

        support.consume("oms", envelope(eventId, "oms.order.created", new OrderCreated("ORD-DUP")));

        assertThat(handled.get()).isNull();   // 멱등 skip
    }

    @Test
    void 핸들러가_없으면_조용히_무시한다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(any(), any())).thenReturn(true);
        // registry에 아무것도 등록하지 않음

        support.consume("oms", envelope(eventId, "oms.order.created", new OrderCreated("ORD-X")));

        // 예외 없이 통과. 컨텍스트도 안 열림(핸들러 없어 조기 return).
        assertThat(FlowContext.get()).isNull();
    }

    @Test
    void envelope_역직렬화_실패는_재시도없는_NonRetryable로_던진다() {
        assertThatThrownBy(() -> support.consume("oms", "not-a-valid-json-envelope"))
                .isInstanceOf(NonRetryableEventException.class)
                .hasMessageContaining("envelope 역직렬화 실패");

        verify(inbox, never()).recordIfNew(any(), any());   // 파싱 전에 실패
    }

    @Test
    void payload_역직렬화_실패도_재시도없는_NonRetryable로_던진다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(any(), any())).thenReturn(true);
        registry.register("oms", OrderCreated.class, e -> {});   // 핸들러는 있어야 payload 역직렬화 단계에 도달
        // eventType은 등록된 타입인데 payload가 그 타입 구조가 아님(배열) → treeToValue 실패
        Envelope broken = new Envelope(eventId, "oms.order.created", 1,
                OffsetDateTime.now(ZoneOffset.UTC), "ORD-B",
                "dongsuh", "DS-GRP", "corr-1", null, mapper.readTree("[1,2,3]"));

        assertThatThrownBy(() -> support.consume("oms", mapper.writeValueAsString(broken)))
                .isInstanceOf(NonRetryableEventException.class)
                .hasMessageContaining("payload 역직렬화 실패");

        assertThat(FlowContext.get()).isNull();   // 실패해도 finally clear
    }

    @Test
    void 핸들러가_던진_예외는_RuntimeException으로_감싸_재시도되게_한다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(any(), any())).thenReturn(true);
        registry.register("oms", OrderCreated.class, e -> { throw new RuntimeException("핸들러 폭발"); });

        assertThatThrownBy(() -> support.consume("oms", envelope(eventId, "oms.order.created", new OrderCreated("ORD-E"))))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("핸들러 처리 실패");

        assertThat(FlowContext.get()).isNull();   // 실패해도 finally clear
    }

    @Test
    void 핸들러가_던진_NonRetryable은_그대로_전파해_즉시_DLT로_보낸다() throws Exception {
        UUID eventId = UUID.randomUUID();
        when(inbox.recordIfNew(any(), any())).thenReturn(true);
        registry.register("oms", OrderCreated.class,
                e -> { throw new NonRetryableEventException("검증 실패", null); });

        assertThatThrownBy(() -> support.consume("oms", envelope(eventId, "oms.order.created", new OrderCreated("ORD-N"))))
                .isInstanceOf(NonRetryableEventException.class);   // RuntimeException으로 안 감싸짐
    }
}
