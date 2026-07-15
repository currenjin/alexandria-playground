package com.wemeet.core.event.consume;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** 재시도 무의미(역직렬화·validation 실패) 예외 — 소비 파이프라인이 이걸 만나면 즉시 DLT로 보낸다. */
class NonRetryableEventExceptionTest {

    @Test
    void 메시지와_원인을_보존한다() {
        Throwable cause = new IllegalArgumentException("bad payload");

        NonRetryableEventException e = new NonRetryableEventException("역직렬화 실패", cause);

        assertThat(e).isInstanceOf(RuntimeException.class);
        assertThat(e.getMessage()).isEqualTo("역직렬화 실패");
        assertThat(e.getCause()).isSameAs(cause);
    }
}
