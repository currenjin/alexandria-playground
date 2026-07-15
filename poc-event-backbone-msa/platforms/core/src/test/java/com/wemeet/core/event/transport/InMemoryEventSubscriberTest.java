package com.wemeet.core.event.transport;

import com.wemeet.core.event.consume.EventConsumerSupport;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * 인메모리 수신 어댑터 — 설정의 subscribe-topics를 버스에서 구독해 공통 소비 파이프라인(consume)으로 넘긴다.
 * Kafka 리스너 경로와 대칭. 콤마 구분 다중 토픽·공백 트림을 확인한다.
 */
class InMemoryEventSubscriberTest {

    @Test
    void 설정_토픽들을_구독해_들어온_메시지를_consume으로_넘긴다() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        EventConsumerSupport support = mock(EventConsumerSupport.class);
        CountDownLatch latch = new CountDownLatch(2);
        List<String> consumed = new CopyOnWriteArrayList<>();
        doAnswer(inv -> { consumed.add(inv.getArgument(1)); latch.countDown(); return null; })
                .when(support).consume(eq("saga"), org.mockito.ArgumentMatchers.any());

        // 공백 포함 콤마 구분 — trim 되어야 한다
        new InMemoryEventSubscriber(bus, support, "saga", "oms.order, tms.dispatch");

        bus.publish("oms.order", "env-1");
        bus.publish("tms.dispatch", "env-2");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(consumed).containsExactlyInAnyOrder("env-1", "env-2");
    }
}
