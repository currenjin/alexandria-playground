package com.wemeet.eventbackbone.common.maintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 보존 배치 (§7.1.3 / §7.1.5) — 발행 완료 outbox 7일·처리 완료 inbox 7일 후 삭제.
 * 미발행 outbox(published_at null)는 무기한 보존(§7.1.3). 영구 감사는 이벤트 스토어의 몫이지 여기 아님.
 */
@Component
public class RetentionCleaner {

    private static final Logger log = LoggerFactory.getLogger(RetentionCleaner.class);

    private final JdbcTemplate jdbc;
    private final int outboxDays;
    private final int inboxDays;

    public RetentionCleaner(JdbcTemplate jdbc,
                            @Value("${platform.events.retention.outbox-days:7}") int outboxDays,
                            @Value("${platform.events.retention.inbox-days:7}") int inboxDays) {
        this.jdbc = jdbc;
        this.outboxDays = outboxDays;
        this.inboxDays = inboxDays;
    }

    @Scheduled(fixedDelayString = "${platform.events.retention.scan-ms:3600000}")   // 1시간
    @Transactional
    public void clean() {
        int ob = jdbc.update(
            "DELETE FROM outbox WHERE published_at IS NOT NULL AND published_at < now() - make_interval(days => ?)",
            outboxDays);
        int ib = jdbc.update(
            "DELETE FROM inbox WHERE processed_at < now() - make_interval(days => ?)",
            inboxDays);
        if (ob > 0 || ib > 0) log.info("보존 정리: outbox {}행, inbox {}행 삭제", ob, ib);
    }
}
