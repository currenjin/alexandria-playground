package com.wemeet.core.maintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 릴레이 감시 지표 (§7.1.4) — age of oldest message(가장 오래된 미발행 이벤트의 나이) = now() - min(created_at) WHERE published_at IS NULL.
 * 릴레이가 멈추면 발행 0인데 에러도 안 나므로, 이 lag 증가로만 이상을 감지한다(건수는 피크에 정상적으로도 높음).
 */
@Component
public class RelayLagMonitor {

    private static final Logger log = LoggerFactory.getLogger(RelayLagMonitor.class);

    private final JdbcTemplate jdbc;
    private final long warnAgeMs;

    public RelayLagMonitor(JdbcTemplate jdbc,
                           @Value("${platform.events.relay.lag-warn-ms:2000}") long warnAgeMs) {
        this.jdbc = jdbc;
        this.warnAgeMs = warnAgeMs;
    }

    @Scheduled(fixedDelayString = "${platform.events.relay.lag-scan-ms:5000}")
    public void checkLag() {
        Long ageMs = jdbc.queryForObject(
            "SELECT COALESCE(EXTRACT(EPOCH FROM (now() - min(created_at))) * 1000, 0)::bigint "
          + "FROM outbox WHERE published_at IS NULL", Long.class);
        if (ageMs != null && ageMs > warnAgeMs) {
            log.warn("age of oldest message {}ms (> {}ms) — 릴레이 지연/정지 의심 (§7.1.4)", ageMs, warnAgeMs);
        }
    }
}
