package com.wemeet.eventbackbone.common.inbox;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Inbox 멱등 저장소 (§7.1.5, 공통 제공). at-least-once 전달의 2차 방어.
 * "이미 처리했나?" 확인과 기록을 <b>한 번의 원자적 INSERT</b>로 합친다(ON CONFLICT DO NOTHING) —
 * 확인 후 기록 사이의 경쟁 조건을 DB unique 제약으로 직렬화. 호출자의 트랜잭션 안에서 실행된다.
 */
@Component
public class InboxRepository {

    private final JdbcTemplate jdbc;

    public InboxRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * 이 (그룹, 이벤트)를 처음 보는 것이면 기록하고 true, 이미 있으면(중복) false.
     * true일 때만 핸들러를 태운다.
     */
    public boolean recordIfNew(String consumerGroup, UUID eventId) {
        int inserted = jdbc.update(
            "INSERT INTO inbox (consumer_group, event_id, processed_at) VALUES (?, ?, now()) ON CONFLICT DO NOTHING",
            consumerGroup, eventId);
        return inserted == 1;
    }
}
