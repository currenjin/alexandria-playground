package com.wemeet.eventbackbone.oms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.common.event.contract.Envelope;
import com.wemeet.eventbackbone.common.event.contract.UuidV7;
import com.wemeet.eventbackbone.contracts.OrderContracts.DispatchOrder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testcontainers 왕복 테스트(§7.1.8). Postgres + Kafka로 실제 백본을 관통한다.
 * 발행: confirm() → outbox INSERT → 릴레이 → Kafka(oms.order)에 envelope 도착.
 * 소비+멱등: oms.cmd로 MarkDispatched → 주문 DISPATCHED + inbox 기록, 같은 eventId 재전송해도 1회만 처리.
 */
@SpringBootTest(classes = OmsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class EventBackboneIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16")).withDatabaseName("oms");

    @Container
    static final org.testcontainers.kafka.KafkaContainer KAFKA =
            new org.testcontainers.kafka.KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);
        r.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    @Autowired com.wemeet.eventbackbone.oms.application.OmsService oms;
    @Autowired ObjectMapper mapper;
    @Autowired JdbcTemplate jdbc;
    @Autowired KafkaTemplate<String, String> kafka;

    @Test
    void full_round_trip_publish_then_consume_with_idempotency() throws Exception {
        String orderId = "ORD-IT-1";

        FlowContext.openEntry("dongsuh", "DS-GRP", null);
        try {
            oms.create(orderId, "SHIPPER-IT", "서울", "부산", "1250000", "KRW");
        } finally {
            FlowContext.clear();
        }
        assertThat(jdbc.queryForObject("SELECT status FROM orders WHERE order_id=?", String.class, orderId))
                .isEqualTo("CREATED");

        String created = pollFor("oms.order", "reader-order",
                v -> v.contains("\"oms.order.created\"") && v.contains(orderId), Duration.ofSeconds(20));
        assertThat(created).contains("\"eventType\":\"oms.order.created\"").contains(orderId);

        UUID cmdEventId = UuidV7.generate();
        String envelope = mapper.writeValueAsString(new Envelope(
                cmdEventId, "oms.cmd.dispatch_order", 1,
                OffsetDateTime.now(ZoneOffset.UTC), orderId, "dongsuh", "DS-GRP",
                UUID.randomUUID().toString(), null,
                mapper.valueToTree(new DispatchOrder(orderId, "DISP-IT"))));

        kafka.send("oms.cmd", orderId, envelope).get();
        waitUntil(Duration.ofSeconds(20), () ->
                "DISPATCHED".equals(jdbc.queryForObject("SELECT status FROM orders WHERE order_id=?", String.class, orderId)));

        Integer inboxCount = jdbc.queryForObject(
                "SELECT count(*) FROM inbox WHERE consumer_group='oms' AND event_id=?", Integer.class, cmdEventId);
        assertThat(inboxCount).isEqualTo(1);

        kafka.send("oms.cmd", orderId, envelope).get();
        Thread.sleep(2000);
        assertThat(jdbc.queryForObject(
                "SELECT count(*) FROM inbox WHERE consumer_group='oms' AND event_id=?", Integer.class, cmdEventId))
                .isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT status FROM orders WHERE order_id=?", String.class, orderId))
                .isEqualTo("DISPATCHED");
    }

    @Test
    void poison_message_is_routed_to_DLT() throws Exception {
        kafka.send("oms.cmd", "ORD-poison", "not-a-valid-envelope").get();
        String dead = pollFor("oms.cmd.DLT", "reader-dlt",
                v -> v.contains("not-a-valid-envelope"), Duration.ofSeconds(20));
        assertThat(dead).contains("not-a-valid-envelope");
    }

    private String pollFor(String topic, String group, java.util.function.Predicate<String> match, Duration timeout) {
        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA.getBootstrapServers());
        p.put(ConsumerConfig.GROUP_ID_CONFIG, group + "-" + UUID.randomUUID());
        p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        try (KafkaConsumer<String, String> c = new KafkaConsumer<>(p, new StringDeserializer(), new StringDeserializer())) {
            c.subscribe(List.of(topic));
            long deadline = System.nanoTime() + timeout.toNanos();
            while (System.nanoTime() < deadline) {
                ConsumerRecords<String, String> recs = c.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> rec : recs) {
                    if (match.test(rec.value())) return rec.value();
                }
            }
        }
        throw new AssertionError("타임아웃: " + topic + " 에서 조건에 맞는 메시지 없음");
    }

    private void waitUntil(Duration timeout, BooleanSupplier cond) throws InterruptedException {
        long deadline = System.nanoTime() + timeout.toNanos();
        while (System.nanoTime() < deadline) {
            try { if (cond.getAsBoolean()) return; } catch (Exception ignore) { }
            Thread.sleep(300);
        }
        throw new AssertionError("타임아웃: 조건 미충족");
    }
}
