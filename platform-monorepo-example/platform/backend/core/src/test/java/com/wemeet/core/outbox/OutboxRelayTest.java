package com.wemeet.core.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.core.event.contract.Envelope;
import com.wemeet.core.event.contract.EventJson;
import com.wemeet.core.event.transport.MessageTransport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Outbox 폴링 릴레이 — 미발행 행을 seq 순서로 브로커에 발행하고 마킹. 전송은 MessageTransport 포트에만 의존.
 * 발행-마킹 사이 실패는 순서를 지키며 멈추고(재발행은 소비 inbox가 흡수), 다음 폴링에 재개한다.
 */
class OutboxRelayTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final MessageTransport transport = mock(MessageTransport.class);
    private final ObjectMapper mapper = EventJson.mapper();
    private final OutboxRelay relay = new OutboxRelay(jdbc, transport, 500);

    private Envelope env(String type, String aggregateId) {
        return new Envelope(UUID.randomUUID(), type, 1,
                OffsetDateTime.now(ZoneOffset.UTC), aggregateId,
                "t", "c", "corr", null, mapper.createObjectNode().put("k", "v"));
    }

    @SuppressWarnings("unchecked")
    private void stubBatch(Envelope... batch) {
        when(jdbc.query(any(String.class), any(RowMapper.class), eq(500)))
                .thenReturn(List.of(batch));
    }

    @Test
    void 미발행_배치를_토픽으로_발행하고_행을_마킹한다() {
        Envelope e = env("oms.order.created", "ORD-1");
        stubBatch(e);

        relay.relay();

        // 토픽 = eventType 앞 두 마디, key = aggregateId
        verify(transport).send(eq("oms.order"), eq("ORD-1"), contains("oms.order.created"));
        verify(jdbc).update(contains("UPDATE outbox SET published_at"), eq(e.eventId()));
    }

    @Test
    void 배치가_여러개면_모두_발행하고_모두_마킹한다() {
        stubBatch(env("oms.order.created", "ORD-1"),
                  env("tms.dispatch.created", "DISP-1"),
                  env("bms.settlement.completed", "SET-1"));

        relay.relay();

        verify(transport).send(eq("oms.order"), eq("ORD-1"), any());
        verify(transport).send(eq("tms.dispatch"), eq("DISP-1"), any());
        verify(transport).send(eq("bms.settlement"), eq("SET-1"), any());
        verify(jdbc, times(3)).update(contains("UPDATE outbox"), any(UUID.class));
    }

    @Test
    void 빈_배치면_아무것도_발행하지_않는다() {
        stubBatch();   // 빈 리스트

        relay.relay();

        verify(transport, never()).send(any(), any(), any());
        verify(jdbc, never()).update(contains("UPDATE outbox"), any(UUID.class));
    }

    @Test
    void 발행_실패하면_그_자리에서_멈춰_순서를_지킨다_다음_폴링에_재개() {
        Envelope first = env("oms.order.created", "ORD-1");
        Envelope second = env("oms.order.dispatched", "ORD-1");
        stubBatch(first, second);
        doThrow(new RuntimeException("broker down"))
                .when(transport).send(eq("oms.order"), any(), any());

        relay.relay();   // 예외를 삼키고 break — 순서 보존

        // 첫 행 발행 시도했지만 실패 → 마킹 안 됨, 둘째는 시도조차 안 함(순서 보존)
        verify(jdbc, never()).update(contains("UPDATE outbox"), any(UUID.class));
        verify(transport, times(1)).send(any(), any(), any());   // second로 진행하지 않음
    }

    @Test
    void 마킹_UPDATE에_해당_eventId를_넘긴다() {
        Envelope e = env("oms.order.created", "ORD-9");
        stubBatch(e);

        relay.relay();

        ArgumentCaptor<UUID> id = ArgumentCaptor.forClass(UUID.class);
        verify(jdbc).update(contains("UPDATE outbox"), id.capture());
        assertThat(id.getValue()).isEqualTo(e.eventId());
    }

    @Test
    @SuppressWarnings("unchecked")
    void 행_매퍼가_ResultSet의_컬럼을_봉투로_옮긴다() throws Exception {
        // relay가 jdbc.query에 넘기는 RowMapper를 잡아, 실제 ResultSet 없이 매핑 로직만 검증한다.
        stubBatch();   // 빈 배치라도 query 호출은 일어나 RowMapper가 캡처된다
        relay.relay();
        RowMapper<Envelope> mapper = captureRowMapper();

        UUID eventId = UUID.randomUUID();
        OffsetDateTime occurred = OffsetDateTime.now(ZoneOffset.UTC);
        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("event_id", UUID.class)).thenReturn(eventId);
        when(rs.getString("event_type")).thenReturn("oms.order.created");
        when(rs.getInt("version")).thenReturn(1);
        when(rs.getObject("occurred_at", OffsetDateTime.class)).thenReturn(occurred);
        when(rs.getString("aggregate_id")).thenReturn("ORD-1");
        when(rs.getString("tenant_id")).thenReturn("dongsuh");
        when(rs.getString("corp_id")).thenReturn("DS-GRP");
        when(rs.getString("correlation_id")).thenReturn("corr-1");
        when(rs.getObject("caused_by_event_id", UUID.class)).thenReturn(null);
        when(rs.getString("payload")).thenReturn("{\"k\":\"v\"}");

        Envelope env = mapper.mapRow(rs, 0);

        assertThat(env.eventId()).isEqualTo(eventId);
        assertThat(env.eventType()).isEqualTo("oms.order.created");
        assertThat(env.aggregateId()).isEqualTo("ORD-1");
        assertThat(env.tenantId()).isEqualTo("dongsuh");
        assertThat(env.payload().get("k").asText()).isEqualTo("v");
    }

    @Test
    void 행_매핑_중_예외는_IllegalState로_감싼다() throws Exception {
        stubBatch();
        relay.relay();
        RowMapper<Envelope> mapper = captureRowMapper();

        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("payload")).thenReturn("not-json");   // readTree 실패 유도

        assertThatThrownBy(() -> mapper.mapRow(rs, 0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("outbox 행 매핑 실패");
    }

    @SuppressWarnings("unchecked")
    private RowMapper<Envelope> captureRowMapper() {
        ArgumentCaptor<RowMapper<Envelope>> captor = ArgumentCaptor.forClass(RowMapper.class);
        verify(jdbc).query(any(String.class), captor.capture(), eq(500));
        return captor.getValue();
    }
}
