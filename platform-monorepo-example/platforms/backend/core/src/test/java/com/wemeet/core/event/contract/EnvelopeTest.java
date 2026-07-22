package com.wemeet.core.event.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 봉투 8필드 record — 개발자는 payload·aggregateId만 채우고 나머지는 공통이 채운다는 계약을 record 구조로 확인.
 * EventJson mapper로 왕복 직렬화가 손실 없이 되는지(릴레이 발신 ↔ 소비 수신 대칭)도 본다.
 */
class EnvelopeTest {

    private final ObjectMapper mapper = EventJson.mapper();

    @Test
    void 여덟_필드_접근자가_그대로_노출된다() {
        UUID eventId = UuidV7.generate();
        UUID caused = UuidV7.generate();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        JsonNode payload = mapper.createObjectNode().put("orderId", "ORD-1");

        Envelope env = new Envelope(eventId, "oms.order.created", 3, now,
                "ORD-1", "dongsuh", "DS-GRP", "corr-1", caused, payload);

        assertThat(env.eventId()).isEqualTo(eventId);
        assertThat(env.eventType()).isEqualTo("oms.order.created");
        assertThat(env.version()).isEqualTo(3);
        assertThat(env.occurredAt()).isEqualTo(now);
        assertThat(env.aggregateId()).isEqualTo("ORD-1");
        assertThat(env.tenantId()).isEqualTo("dongsuh");
        assertThat(env.corpId()).isEqualTo("DS-GRP");
        assertThat(env.correlationId()).isEqualTo("corr-1");
        assertThat(env.causedByEventId()).isEqualTo(caused);
        assertThat(env.payload()).isEqualTo(payload);
    }

    @Test
    void EventJson_mapper로_왕복_직렬화가_손실없다() throws Exception {
        UUID eventId = UuidV7.generate();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        JsonNode payload = mapper.createObjectNode().put("orderId", "ORD-2");
        Envelope env = new Envelope(eventId, "oms.order.created", 1, now,
                "ORD-2", "t", "c", "corr", null, payload);

        String json = mapper.writeValueAsString(env);
        Envelope back = mapper.readValue(json, Envelope.class);

        assertThat(back.eventId()).isEqualTo(eventId);
        assertThat(back.eventType()).isEqualTo("oms.order.created");
        assertThat(back.aggregateId()).isEqualTo("ORD-2");
        assertThat(back.causedByEventId()).isNull();
        assertThat(back.payload().get("orderId").asText()).isEqualTo("ORD-2");
    }
}
