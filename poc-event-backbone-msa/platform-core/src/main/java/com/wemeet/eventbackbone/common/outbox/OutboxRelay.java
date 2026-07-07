package com.wemeet.eventbackbone.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.event.Envelope;
import com.wemeet.eventbackbone.common.event.EventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * §7.1.4 폴링 릴레이 — 커밋된 outbox 미발행 행을 seq 순서로 Kafka에 발행 후 마킹.
 * FOR UPDATE SKIP LOCKED로 다중 인스턴스 중복 발행 방지. 발행 후 마킹 전 크래시 → 재발행(중복)은
 * 소비 측 Inbox(§7.1.5)가 막는다 = at-least-once.
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final JdbcTemplate jdbc;
    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;
    private final int batchSize;

    public OutboxRelay(JdbcTemplate jdbc, KafkaTemplate<String, String> kafka, ObjectMapper mapper,
                       @Value("${platform.events.relay.batch-size:500}") int batchSize) {
        this.jdbc = jdbc;
        this.kafka = kafka;
        this.mapper = mapper;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${platform.events.relay.poll-interval-ms:200}")
    @Transactional   // FOR UPDATE SKIP LOCKED 락을 발행·마킹까지 유지 (다중 인스턴스 안전)
    public void relay() {
        List<Map<String, Object>> rows = jdbc.queryForList(
            "SELECT event_id, event_type, version, occurred_at, aggregate_id, tenant_id, corp_id, "
          + "correlation_id, caused_by_event_id, payload::text AS payload "
          + "FROM outbox WHERE published_at IS NULL ORDER BY seq LIMIT ? FOR UPDATE SKIP LOCKED",
            batchSize);
        for (Map<String, Object> r : rows) {
            try {
                String eventType = (String) r.get("event_type");
                String topic = EventTypes.topicOf(eventType);
                String key = (String) r.get("aggregate_id");
                String envelopeJson = mapper.writeValueAsString(toEnvelope(r));
                kafka.send(topic, key, envelopeJson).get();   // acks=all 대기
                jdbc.update("UPDATE outbox SET published_at = now() WHERE event_id = ?", r.get("event_id"));
                log.debug("relay -> {} key={} type={}", topic, key, eventType);
            } catch (Exception e) {
                // 순서 보존: 실패 지점에서 멈추고 다음 주기에 seq 순서로 재시도 (§7.1.3/§7.1.4)
                log.warn("발행 실패, 다음 폴링에 재개: {}", r.get("event_id"), e);
                break;
            }
        }
    }

    private Envelope toEnvelope(Map<String, Object> r) throws Exception {
        return new Envelope(
            (UUID) r.get("event_id"),
            (String) r.get("event_type"),
            ((Number) r.get("version")).intValue(),
            ((Timestamp) r.get("occurred_at")).toInstant().atOffset(ZoneOffset.UTC),
            (String) r.get("aggregate_id"),
            (String) r.get("tenant_id"),
            (String) r.get("corp_id"),
            (String) r.get("correlation_id"),
            (UUID) r.get("caused_by_event_id"),
            mapper.readTree((String) r.get("payload"))
        );
    }
}
