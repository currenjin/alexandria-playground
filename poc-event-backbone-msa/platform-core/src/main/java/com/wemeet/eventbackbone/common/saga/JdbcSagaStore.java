package com.wemeet.eventbackbone.common.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * saga_instance 어댑터 (§7.1.7, 공통 엔진 구현). saga_instance 테이블은 중앙 orchestrator 서비스 DB에만 존재.
 */
@Repository
public class JdbcSagaStore implements SagaStore {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    public JdbcSagaStore(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public void start(String sagaType, String aggregateId, String correlationId,
                      Map<String, Object> state, OffsetDateTime timeoutAt) {
        jdbc.update("""
            INSERT INTO saga_instance (saga_id, saga_type, aggregate_id, correlation_id, current_step, status, state, timeout_at)
            VALUES (?, ?, ?, ?, 'dispatch', 'STARTED', ?::jsonb, ?)
            """, UUID.randomUUID(), sagaType, aggregateId, correlationId, toJson(state), timeoutAt);
    }

    @Override
    public void advance(String correlationId, String status, String currentStep,
                        Map<String, Object> state, OffsetDateTime timeoutAt) {
        jdbc.update("UPDATE saga_instance SET status=?, current_step=?, state=?::jsonb, timeout_at=?, updated_at=now() WHERE correlation_id=?",
                status, currentStep, toJson(state), timeoutAt, correlationId);
    }

    @Override
    public void finish(String correlationId, String status) {
        jdbc.update("UPDATE saga_instance SET status=?, current_step=null, timeout_at=null, updated_at=now() WHERE correlation_id=?",
                status, correlationId);
    }

    @Override
    public Map<String, Object> state(String correlationId) {
        String json = jdbc.queryForObject("SELECT state::text FROM saga_instance WHERE correlation_id=?", String.class, correlationId);
        return fromJson(json);
    }

    @Override
    public List<TimedOut> findTimedOut() {
        return jdbc.query(
            "SELECT correlation_id, aggregate_id, current_step, status FROM saga_instance "
          + "WHERE status IN ('STARTED','DISPATCHED') AND timeout_at < now() FOR UPDATE SKIP LOCKED",
            (rs, i) -> new TimedOut(rs.getString("correlation_id"), rs.getString("aggregate_id"),
                                    rs.getString("current_step"), rs.getString("status")));
    }

    @Override
    public String status(String correlationId) {
        return jdbc.query("SELECT status FROM saga_instance WHERE correlation_id=?",
                (rs, i) -> rs.getString("status"), correlationId)
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
