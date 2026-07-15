package com.wemeet.core.inbox;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * inbox 멱등 — 확인+기록을 원자적 INSERT 하나(ON CONFLICT DO NOTHING)로 합친다.
 * INSERT 결과 행수로 "처음 보는 이벤트"인지 판정: 1이면 새것(true), 0이면 이미 처리됨(false).
 */
class InboxRepositoryTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final InboxRepository inbox = new InboxRepository(jdbc);

    @Test
    void 처음_보는_이벤트면_기록하고_true() {
        UUID eventId = UUID.randomUUID();
        when(jdbc.update(any(String.class), eq("oms"), eq(eventId))).thenReturn(1);

        boolean isNew = inbox.recordIfNew("oms", eventId);

        assertThat(isNew).isTrue();
    }

    @Test
    void 이미_있는_이벤트면_false_중복() {
        UUID eventId = UUID.randomUUID();
        when(jdbc.update(any(String.class), eq("oms"), eq(eventId))).thenReturn(0);   // ON CONFLICT DO NOTHING

        boolean isNew = inbox.recordIfNew("oms", eventId);

        assertThat(isNew).isFalse();
    }

    @Test
    void consumerGroup과_eventId를_INSERT_인자로_넘긴다() {
        UUID eventId = UUID.randomUUID();
        when(jdbc.update(any(String.class), any(), any())).thenReturn(1);

        inbox.recordIfNew("saga", eventId);

        verify(jdbc).update(any(String.class), eq("saga"), eq(eventId));
    }
}
