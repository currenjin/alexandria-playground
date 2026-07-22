package com.wemeet.core.saga;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * saga_instance 어댑터(중앙 orchestrator DB). 매칭 열쇠 = aggregateId(업무 키). state는 jsonb로 저장.
 * status()/state() 조회, findTimedOut(만료분), finish(종착) 등 사가 엔진이 쓰는 저장 포트 구현을 검증.
 */
class JdbcSagaStoreTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final JdbcSagaStore store = new JdbcSagaStore(jdbc);

    @Test
    void start는_INSERT하고_state를_json으로_직렬화한다() {
        OffsetDateTime timeout = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(5);

        store.start("dispatch-saga", "ORD-1", "corr-1", "STARTED", "await_dispatch",
                Map.of("orderId", "ORD-1"), timeout);

        ArgumentCaptor<Object> args = ArgumentCaptor.forClass(Object.class);
        // saga_id, saga_type, aggregate_id, correlation_id, current_step, status, state, timeout_at
        verify(jdbc).update(contains("INSERT INTO saga_instance"),
                args.capture(), args.capture(), args.capture(), args.capture(),
                args.capture(), args.capture(), args.capture(), args.capture());
        var v = args.getAllValues();
        assertThat(v.get(1)).isEqualTo("dispatch-saga");
        assertThat(v.get(2)).isEqualTo("ORD-1");
        assertThat(v.get(3)).isEqualTo("corr-1");
        assertThat(v.get(4)).isEqualTo("await_dispatch");
        assertThat(v.get(5)).isEqualTo("STARTED");
        assertThat(v.get(6).toString()).contains("orderId").contains("ORD-1");   // state json
        assertThat(v.get(7)).isEqualTo(timeout);
    }

    @Test
    void advance는_aggregateId로_UPDATE한다() {
        store.advance("ORD-2", "IN_PROGRESS", "await_delivery", Map.of("k", "v"), null);

        ArgumentCaptor<Object> args = ArgumentCaptor.forClass(Object.class);
        verify(jdbc).update(contains("UPDATE saga_instance SET status="),
                args.capture(), args.capture(), args.capture(), args.capture(), args.capture());
        var v = args.getAllValues();
        assertThat(v.get(0)).isEqualTo("IN_PROGRESS");
        assertThat(v.get(1)).isEqualTo("await_delivery");
        assertThat(v.get(4)).isEqualTo("ORD-2");   // WHERE aggregate_id
    }

    @Test
    void finish는_step과_timeout을_null로_비우며_종착_상태로_UPDATE한다() {
        store.finish("ORD-3", "COMPLETED");

        verify(jdbc).update(contains("current_step=null"), eq("COMPLETED"), eq("ORD-3"));
    }

    @Test
    void state는_json을_Map으로_역직렬화한다() {
        when(jdbc.queryForObject(contains("SELECT state::text"), eq(String.class), eq("ORD-4")))
                .thenReturn("{\"orderId\":\"ORD-4\",\"amount\":\"1000\"}");

        Map<String, Object> state = store.state("ORD-4");

        assertThat(state).containsEntry("orderId", "ORD-4").containsEntry("amount", "1000");
    }

    @Test
    void status는_행이_있으면_상태문자열을_돌려준다() {
        stubStatusQuery("ORD-5", List.of("IN_PROGRESS"));

        assertThat(store.status("ORD-5")).isEqualTo("IN_PROGRESS");
    }

    @Test
    void status는_행이_없으면_null_종료가드용() {
        stubStatusQuery("ORD-NONE", List.of());

        assertThat(store.status("ORD-NONE")).isNull();
    }

    @Test
    void state가_깨진_json이면_RuntimeException으로_감싼다() {
        when(jdbc.queryForObject(contains("SELECT state::text"), eq(String.class), eq("ORD-BAD")))
                .thenReturn("not-json");

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> store.state("ORD-BAD"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findTimedOut은_만료된_인스턴스를_TimedOut로_매핑한다() {
        stubTimedOutQuery(List.of(new SagaStore.TimedOut("ORD-6", "await_dispatch", "IN_PROGRESS")));

        List<SagaStore.TimedOut> out = store.findTimedOut();

        assertThat(out).hasSize(1);
        assertThat(out.get(0).aggregateId()).isEqualTo("ORD-6");
        assertThat(out.get(0).currentStep()).isEqualTo("await_dispatch");
        assertThat(out.get(0).status()).isEqualTo("IN_PROGRESS");
    }

    @SuppressWarnings("unchecked")
    private void stubStatusQuery(String aggregateId, List<String> result) {
        when(jdbc.query(contains("SELECT status FROM saga_instance"), any(RowMapper.class), eq(aggregateId)))
                .thenReturn(result);
    }

    @SuppressWarnings("unchecked")
    private void stubTimedOutQuery(List<SagaStore.TimedOut> result) {
        when(jdbc.query(contains("FOR UPDATE SKIP LOCKED"), any(RowMapper.class)))
                .thenReturn(result);
    }
}
