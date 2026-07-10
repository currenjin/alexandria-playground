package com.wemeet.eventbackbone.common.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.event.contract.Envelope;
import com.wemeet.eventbackbone.common.event.contract.EventTypes;
import com.wemeet.eventbackbone.common.event.transport.MessageTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Outbox 폴링 릴레이. 미발행 행을 seq 순서로 브로커에 발행하고 마킹한다.
 * 전송은 {@link MessageTransport} 포트에만 의존한다 — 브로커(Kafka→SQS 등) 교체 지점.
 * {@code FOR UPDATE SKIP LOCKED}로 다중 인스턴스에서도 중복 발행이 없고, 발행-마킹 사이 크래시로 인한
 * 재발행은 소비 측 inbox가 흡수한다(at-least-once).
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private static final String SELECT_UNPUBLISHED = """
        SELECT event_id, event_type, version, occurred_at, aggregate_id, tenant_id, corp_id,
               correlation_id, caused_by_event_id, payload::text AS payload
        FROM outbox WHERE published_at IS NULL ORDER BY seq LIMIT ? FOR UPDATE SKIP LOCKED""";

    private final JdbcTemplate jdbc;
    private final MessageTransport transport;
    private final ObjectMapper mapper;
    private final int batchSize;
    private final RowMapper<Envelope> envelopeMapper;

    public OutboxRelay(JdbcTemplate jdbc, MessageTransport transport, ObjectMapper mapper,
                       @Value("${platform.events.relay.batch-size:500}") int batchSize) {
        this.jdbc = jdbc;
        this.transport = transport;
        this.mapper = mapper;
        this.batchSize = batchSize;
        this.envelopeMapper = (rs, n) -> {
            try {
                return new Envelope(
                        rs.getObject("event_id", UUID.class),
                        rs.getString("event_type"),
                        rs.getInt("version"),
                        rs.getObject("occurred_at", OffsetDateTime.class),
                        rs.getString("aggregate_id"),
                        rs.getString("tenant_id"),
                        rs.getString("corp_id"),
                        rs.getString("correlation_id"),
                        rs.getObject("caused_by_event_id", UUID.class),
                        mapper.readTree(rs.getString("payload")));
            } catch (Exception e) {
                throw new IllegalStateException("outbox 행 매핑 실패", e);
            }
        };
    }

    @Scheduled(fixedDelayString = "${platform.events.relay.poll-interval-ms:200}")
    @Transactional
    public void relay() {
        List<Envelope> batch = jdbc.query(SELECT_UNPUBLISHED, envelopeMapper, batchSize);
        for (Envelope env : batch) {
            try {
                String topic = EventTypes.topicOf(env.eventType());
                transport.send(topic, env.aggregateId(), mapper.writeValueAsString(env));
                jdbc.update("UPDATE outbox SET published_at = now() WHERE event_id = ?", env.eventId());
                log.debug("relay -> {} key={} type={}", topic, env.aggregateId(), env.eventType());
            } catch (Exception e) {
                log.warn("relay 실패, 다음 폴링에 재개: {}", env.eventId(), e);
                break;
            }
        }
    }
}
