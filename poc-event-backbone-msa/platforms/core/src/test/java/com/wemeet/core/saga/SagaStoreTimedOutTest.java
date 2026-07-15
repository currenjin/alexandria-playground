package com.wemeet.core.saga;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** TimedOut 값 record — 만료 사가의 (aggregateId, currentStep, status) 3필드 접근자. */
class SagaStoreTimedOutTest {

    @Test
    void 세_필드_접근자() {
        SagaStore.TimedOut t = new SagaStore.TimedOut("ORD-1", "await_dispatch", "IN_PROGRESS");

        assertThat(t.aggregateId()).isEqualTo("ORD-1");
        assertThat(t.currentStep()).isEqualTo("await_dispatch");
        assertThat(t.status()).isEqualTo("IN_PROGRESS");
    }
}
