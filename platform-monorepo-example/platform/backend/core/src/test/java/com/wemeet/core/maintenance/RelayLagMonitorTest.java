package com.wemeet.core.maintenance;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 릴레이 감시 — age of oldest unpublished(가장 오래된 미발행 이벤트 나이)가 임계값을 넘으면 WARN.
 * 릴레이가 멈추면 발행 0인데 에러도 안 나므로 이 lag 증가로만 이상을 감지한다.
 * (로그 부수효과라 예외 없이 임계 위/아래 경로가 도는지만 검증한다.)
 */
class RelayLagMonitorTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);

    private void stubAge(long ageMs) {
        when(jdbc.queryForObject(any(String.class), eq(Long.class))).thenReturn(ageMs);
    }

    @Test
    void lag이_임계값을_넘으면_경로를_탄다_WARN() {
        RelayLagMonitor monitor = new RelayLagMonitor(jdbc, 2000);
        stubAge(5000);   // 2000ms 초과

        monitor.checkLag();   // WARN 로그 — 예외 없이 통과
    }

    @Test
    void lag이_임계값_이하면_조용하다() {
        RelayLagMonitor monitor = new RelayLagMonitor(jdbc, 2000);
        stubAge(100);   // 임계 이하

        monitor.checkLag();
    }

    @Test
    void 미발행이_없어_ageMs가_0이면_경고하지_않는다() {
        RelayLagMonitor monitor = new RelayLagMonitor(jdbc, 2000);
        stubAge(0);   // COALESCE(..., 0)

        monitor.checkLag();
    }

    @Test
    void ageMs가_null이어도_NPE없이_통과한다() {
        RelayLagMonitor monitor = new RelayLagMonitor(jdbc, 2000);
        when(jdbc.queryForObject(any(String.class), eq(Long.class))).thenReturn(null);

        monitor.checkLag();   // ageMs != null 가드
    }
}
