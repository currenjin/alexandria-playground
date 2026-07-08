package com.wemeet.eventbackbone.common.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.contracts.DomainEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * §7.1.1·§7.1.3 — publish() = 봉투 조립 후 OUTBOX INSERT (도메인 변경과 같은 트랜잭션).
 * 활성 트랜잭션 밖 호출은 곧 원자성이 깨지므로 컨텍스트 없으면 즉시 예외 (§7.1.3).
 */
@Component
public class OutboxEventPublisher implements EventPublisher {

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper;

    public OutboxEventPublisher(JdbcTemplate jdbc, ObjectMapper mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    @Override
    public void publish(DomainEvent event) {
        // §7.1.3: 활성 트랜잭션 밖 publish = 즉시 예외. 자체 트랜잭션으로 감싸지 않는다
        // (감싸면 도메인 변경과 분리돼 이중 쓰기 사고가 되살아남).
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException(
                "활성 트랜잭션 밖 publish 금지 (§7.1.3) — 도메인 변경과 같은 @Transactional 안에서 호출하세요");
        }
        FlowContext.Ctx ctx = FlowContext.get();
        if (ctx == null) {
            // §7.1.1: 컨텍스트 없는 publish = 즉시 예외 (조용한 null 금지)
            throw new IllegalStateException("FlowContext 없음 — 진입점/소비 컨텍스트 안에서 publish 하세요");
        }
        UUID eventId = UuidV7.generate();  // §7.1.1 시간순 정렬 UUIDv7
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
