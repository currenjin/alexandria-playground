package com.wemeet.eventbackbone.common.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.event.contract.EventJson;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * saga_instance 어댑터 (§7.1.7, 공통 엔진 구현). saga_instance 테이블은 중앙 orchestrator 서비스 DB에만 존재.
 * 조회·갱신 열쇠 = aggregate_id(업무 키, 예: orderId). correlation_id는 추적용.
 */
@Repository
public class JdbcSagaStore implements SagaStore {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper = EventJson.mapper();

    public JdbcSagaStore(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void start(String sagaType, String aggregateId, String correlationId,
                      String status, String currentStep, Map<String, Object> state, OffsetDateTime timeoutAt) {
        jdbc.update("""
            INSERT INTO saga_instance (saga_id, saga_type, aggregate_id, correlation_id, current_step, status, state, timeout_at)
            VALUES (?, ?, ?, ?, ?, ?, ?::jsonb, ?)
            """, UUID.randomUUID(), sagaType, aggregateId, correlationId, currentStep, status, toJson(state), timeoutAt);
    }

    @Override
    public void advance(String aggregateId, String status, String currentStep,
                        Map<String, Object> state, OffsetDateTime timeoutAt) {
        jdbc.update("UPDATE saga_instance SET status=?, current_step=?, state=?::jsonb, timeout_at=?, updated_at=now() WHERE aggregate_id=?",
                status, currentStep, toJson(state), timeoutAt, aggregateId);
    }

    @Override
    public void finish(String aggregateId, String status) {
        jdbc.update("UPDATE saga_instance SET status=?, current_step=null, timeout_at=null, updated_at=now() WHERE aggregate_id=?",
                status, aggregateId);
    }

    @Override
    public Map<String, Object> state(String aggregateId) {
        String json = jdbc.queryForObject("SELECT state::text FROM saga_instance WHERE aggregate_id=?", String.class, aggregateId);
        return fromJson(json);
    }

    @Override
    public List<TimedOut> findTimedOut() {
        return jdbc.query(
            "SELECT aggregate_id, current_step, status FROM saga_instance "
          + "WHERE timeout_at IS NOT NULL AND timeout_at < now() FOR UPDATE SKIP LOCKED",
            (rs, i) -> new TimedOut(rs.getString("aggregate_id"), rs.getString("current_step"), rs.getString("status")));
    }

    @Override
    public String status(String aggregateId) {
        return jdbc.query("SELECT status FROM saga_instance WHERE aggregate_id=?",
                (rs, i) -> rs.getString("status"), aggregateId)
                .stream().findFirst().orElse(null);
    }

    private String toJson(Map<String, Object> m) {
        try { return mapper.writeValueAsString(m); } catch (Exception e) { throw new RuntimeException(e); }
    }
    @SuppressWarnings("unchecked")
    private Map<String, Object> fromJson(String json) {
        try { return mapper.readValue(json, Map.class); } catch (Exception e) { throw new RuntimeException(e); }
    }
}
