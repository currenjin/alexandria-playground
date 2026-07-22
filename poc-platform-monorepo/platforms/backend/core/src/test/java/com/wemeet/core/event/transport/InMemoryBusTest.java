package com.wemeet.core.event.transport;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 인메모리 브로커(broker=inmemory) — 인프로세스 pub/sub, 비동기 배달로 브로커 흉내.
 * 모듈러 모놀리식/테스트에서 Kafka 없이 백본을 돌리는 용도. 구독자 소비 예외는 격리한다(재시도/DLT 없음).
 */
class InMemoryBusTest {

    @Test
    void publish는_구독자에게_비동기로_배달된다() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        CountDownLatch latch = new CountDownLatch(1);
        List<String> received = new CopyOnWriteArrayList<>();
        bus.subscribe("oms.order", v -> { received.add(v); latch.countDown(); });

        bus.publish("oms.order", "payload-1");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(received).containsExactly("payload-1");
    }

    @Test
    void 한_토픽에_여러_구독자면_모두_받는다() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        CountDownLatch latch = new CountDownLatch(2);
        List<String> a = new CopyOnWriteArrayList<>();
        List<String> b = new CopyOnWriteArrayList<>();
        bus.subscribe("t", v -> { a.add(v); latch.countDown(); });
        bus.subscribe("t", v -> { b.add(v); latch.countDown(); });

        bus.publish("t", "msg");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(a).containsExactly("msg");
        assertThat(b).containsExactly("msg");
    }

    @Test
    void 구독자가_없는_토픽에_publish해도_조용히_무시한다() {
        InMemoryBus bus = new InMemoryBus();

        bus.publish("no.subscriber", "msg");   // 예외 없음
    }

    @Test
    void 구독자_예외는_격리되어_다른_구독자에_전파되지_않는다() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        CountDownLatch latch = new CountDownLatch(2);
        List<String> healthy = new CopyOnWriteArrayList<>();
        bus.subscribe("t", v -> { latch.countDown(); throw new RuntimeException("소비 실패"); });
        bus.subscribe("t", v -> { healthy.add(v); latch.countDown(); });

        bus.publish("t", "msg");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(healthy).containsExactly("msg");   // 실패한 구독자와 무관하게 건강한 구독자는 받음
    }

    @Test
    void 발신_어댑터_send가_버스_publish로_이어진다() throws Exception {
        InMemoryBus bus = new InMemoryBus();
        InMemoryMessageTransport transport = new InMemoryMessageTransport(bus);
        CountDownLatch latch = new CountDownLatch(1);
        List<String> received = new CopyOnWriteArrayList<>();
        bus.subscribe("oms.cmd", v -> { received.add(v); latch.countDown(); });

        transport.send("oms.cmd", "ORD-1", "envelope-json");

        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();
        assertThat(received).containsExactly("envelope-json");
    }
}
