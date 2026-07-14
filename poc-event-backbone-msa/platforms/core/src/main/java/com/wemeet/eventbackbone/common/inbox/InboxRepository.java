package com.wemeet.eventbackbone.common.inbox;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * inbox 멱등 저장소. 처리 여부 확인과 기록을 원자적 INSERT 하나(ON CONFLICT DO NOTHING)로 합쳐,
 * at-least-once 전달에서 새는 중복을 DB unique 제약으로 직렬화한다. 호출자의 트랜잭션 안에서 실행된다.
 */
@Component
public class InboxRepository {

    private final JdbcTemplate jdbc;

    public InboxRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** 처음 보는 (그룹, eventId)이면 기록하고 true, 이미 있으면 false. */
    public boolean recordIfNew(String consumerGroup, UUID eventId) {
        int inserted = jdbc.update(
            "INSERT INTO inbox (consumer_group, event_id, processed_at) VALUES (?, ?, now()) ON CONFLICT DO NOTHING",
            consumerGroup, eventId);
        return inserted == 1;
    }
}
