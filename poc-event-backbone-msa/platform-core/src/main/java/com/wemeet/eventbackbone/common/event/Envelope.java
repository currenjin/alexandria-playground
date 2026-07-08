package com.wemeet.eventbackbone.common.event;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 이벤트 envelope 8필드(§7.1.1). 개발자는 payload와 aggregateId만 채우고, 나머지는 공통이 컨텍스트에서 채운다.
 * eventType은 논리명, eventId는 UUIDv7, occurredAt은 UTC, 금액은 payload에서 문자열+currency로 다룬다.
 */
public record Envelope(
        UUID eventId,
        String eventType,
        int version,
        OffsetDateTime occurredAt,
        String aggregateId,
        String tenantId,
        String corpId,
        String correlationId,
        UUID causedByEventId,
        JsonNode payload
) {}
