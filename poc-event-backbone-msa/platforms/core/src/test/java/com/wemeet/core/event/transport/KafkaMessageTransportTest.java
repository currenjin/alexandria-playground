package com.wemeet.core.event.transport;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Kafka 전송 어댑터 — acks 대기까지 동기(get())로 보내고, 실패하면 예외를 던져
 * 릴레이가 순서를 지키며 멈추게 한다(재발행은 소비 inbox가 흡수).
 */
class KafkaMessageTransportTest {

    @SuppressWarnings("unchecked")
    private final KafkaTemplate<String, String> kafka = mock(KafkaTemplate.class);
    private final KafkaMessageTransport transport = new KafkaMessageTransport(kafka);

    @Test
    void 성공하면_topic_key_payload로_동기_발행한다() {
        CompletableFuture<SendResult<String, String>> done = CompletableFuture.completedFuture(null);
        when(kafka.send(eq("oms.order"), eq("ORD-1"), eq("payload"))).thenReturn(done);

        assertThatCode(() -> transport.send("oms.order", "ORD-1", "payload"))
                .doesNotThrowAnyException();

        verify(kafka).send("oms.order", "ORD-1", "payload");
    }

    @Test
    void 발행이_실패하면_IllegalState로_감싸_던진다_릴레이_정지_유도() {
        CompletableFuture<SendResult<String, String>> failed = new CompletableFuture<>();
        failed.completeExceptionally(new RuntimeException("broker down"));
        when(kafka.send(eq("oms.order"), eq("ORD-1"), eq("payload"))).thenReturn(failed);

        assertThatThrownBy(() -> transport.send("oms.order", "ORD-1", "payload"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Kafka 발행 실패");
    }
}
