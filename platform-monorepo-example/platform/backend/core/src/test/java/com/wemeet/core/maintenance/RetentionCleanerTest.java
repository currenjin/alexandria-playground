package com.wemeet.core.maintenance;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 보존 배치 — 발행 완료 outbox·처리 완료 inbox를 N일 후 삭제. 미발행 outbox는 무기한 보존.
 * outbox-days/inbox-days 설정이 각 DELETE의 make_interval 인자로 흐르는지, 두 테이블을 모두 청소하는지 본다.
 */
class RetentionCleanerTest {

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);

    @Test
    void outbox와_inbox_둘_다_설정된_보존일로_삭제한다() {
        RetentionCleaner cleaner = new RetentionCleaner(jdbc, 7, 14);
        when(jdbc.update(contains("DELETE FROM outbox"), eq(7))).thenReturn(3);
        when(jdbc.update(contains("DELETE FROM inbox"), eq(14))).thenReturn(5);

        cleaner.clean();

        // 발행 완료(published_at not null)만 삭제하는 outbox DELETE
        verify(jdbc).update(contains("published_at IS NOT NULL"), eq(7));
        verify(jdbc).update(contains("DELETE FROM inbox"), eq(14));
    }

    @Test
    void 삭제할_것이_없어도_예외없이_통과한다() {
        RetentionCleaner cleaner = new RetentionCleaner(jdbc, 7, 7);
        when(jdbc.update(contains("DELETE FROM outbox"), eq(7))).thenReturn(0);
        when(jdbc.update(contains("DELETE FROM inbox"), eq(7))).thenReturn(0);

        cleaner.clean();   // log.info 스킵 경로

        verify(jdbc).update(contains("DELETE FROM outbox"), eq(7));
        verify(jdbc).update(contains("DELETE FROM inbox"), eq(7));
    }
}
