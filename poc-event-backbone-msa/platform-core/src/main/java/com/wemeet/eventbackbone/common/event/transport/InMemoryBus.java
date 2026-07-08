package com.wemeet.eventbackbone.common.event.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 인메모리 브로커 (broker=inmemory) — 단일 프로세스용 인프로세스 pub/sub. 비동기 배달로 브로커를 흉내낸다.
 * 모듈러 모놀리식/테스트에서 Kafka 없이 이벤트 백본이 돌게 하는 용도(MSA 다중 프로세스엔 Kafka).
 */
@Component
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "inmemory")
public class InMemoryBus {

    private static final Logger log = LoggerFactory.getLogger(InMemoryBus.class);
    private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();
    private final ExecutorService delivery = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "inmemory-bus");
        t.setDaemon(true);
        return t;
    });

    public void subscribe(String topic, Consumer<String> handler) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public void publish(String topic, String value) {
        for (Consumer<String> h : subscribers.getOrDefault(topic, List.of())) {
            delivery.submit(() -> {
                try {
                    h.accept(value);
                } catch (Exception e) {
                    log.warn("inmemory 소비 실패(재시도/DLT 없음 — 데모 단순화): {}", topic, e);
                }
            });
        }
    }
}
