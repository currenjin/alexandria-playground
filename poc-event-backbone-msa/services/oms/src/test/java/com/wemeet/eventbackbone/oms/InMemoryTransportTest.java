package com.wemeet.eventbackbone.oms;

import com.wemeet.eventbackbone.common.event.transport.InMemoryBus;
import com.wemeet.eventbackbone.common.event.transport.InMemoryMessageTransport;
import com.wemeet.eventbackbone.common.event.transport.MessageTransport;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/** 브로커 이식성 — 인메모리 어댑터로 발신 포트 send -> 수신 왕복(Kafka 없이). 포트 추상화 증명. */
class InMemoryTransportTest {

    @Test
    void inMemory_transport_delivers_send_to_subscriber() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        MessageTransport transport = new InMemoryMessageTransport(bus);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> received = new AtomicReference<>();
        bus.subscribe("oms.cmd", v -> { received.set(v); latch.countDown(); });

        transport.send("oms.cmd", "ORD-1", "{\"eventType\":\"oms.cmd.cancel_order\"}");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(received.get()).contains("cancel_order");
    }
}
