package com.wemeet.core.event.publish;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventJson;
import com.wemeet.core.event.contract.EventTypes;
import com.wemeet.core.event.contract.UuidV7;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.core.context.FlowContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * 이벤트 발행 = envelope 조립 후 outbox INSERT(도메인 변경과 같은 트랜잭션).
 * 활성 트랜잭션 밖에서 부르면 이중 쓰기가 되살아나므로 즉시 예외.
 */
@Component
public class OutboxEventPublisher implements EventPublisher {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper = EventJson.mapper();

    public OutboxEventPublisher(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void publish(DomainEvent event) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("활성 트랜잭션 안에서만 publish 가능 — 도메인 변경과 같은 트랜잭션에서 호출하세요");
        }
        FlowContext.Ctx ctx = FlowContext.get();
        if (ctx == null) {
            throw new IllegalStateException("FlowContext 없음 — 진입점/소비 컨텍스트 안에서 publish 하세요");
        }
        UUID eventId = UuidV7.generate();
        String type = EventTypes.typeOf(event.getClass());
        try {
            String payloadJson = mapper.writeValueAsString(event);
            jdbc.update("""
                INSERT INTO outbox (event_id, event_type, aggregate_id, tenant_id, corp_id,
                                    correlation_id, caused_by_event_id, occurred_at, version, payload)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb)
                """,
                eventId, type, event.aggregateId(), ctx.tenantId(), ctx.corpId(),
                ctx.correlationId(), ctx.currentEventId(),
                OffsetDateTime.now(ZoneOffset.UTC), EventTypes.versionOf(event.getClass()), payloadJson);
        } catch (Exception e) {
            throw new RuntimeException("이벤트 직렬화/기록 실패: " + type, e);
        }
    }
}
