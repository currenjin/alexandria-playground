package com.wemeet.eventbackbone.common.event;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 이벤트 봉투 8필드 (§7.1.1). 개발자가 채우는 건 payload와 aggregateId뿐,
 * 나머지 일곱은 공통(발행자)이 컨텍스트에서 자동으로 채운다.
 */
public record Envelope(
        UUID eventId,                 // UUIDv7 권장 (예제는 randomUUID)
        String eventType,             // 논리명 (FQCN 금지)
        int version,
        OffsetDateTime occurredAt,    // UTC
        String aggregateId,           // 파티션 키
        String tenantId,
        String corpId,
        String correlationId,
        UUID causedByEventId,         // 구 causationId — 직전 원인 이벤트
        JsonNode payload
) {}
