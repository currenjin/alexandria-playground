package com.wemeet.core.event.publish;

import com.wemeet.core.context.FlowContext;
import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventContract;
import com.wemeet.core.event.contract.EventTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * 발행 = envelope 조립 후 outbox INSERT(도메인 변경과 같은 트랜잭션).
 * 활성 TX 밖·컨텍스트 없음이면 이중 쓰기가 되살아나므로 즉시 예외 — 여기서 그 가드를 검증한다.
 */
class OutboxEventPublisherTest {

    @EventContract(type = "oms.order.created", version = 4)
    record OrderCreated(String orderId, String amount) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }

    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final OutboxEventPublisher publisher = new OutboxEventPublisher(jdbc);

    @BeforeEach
    void registerType() {
        EventTypes.register(OrderCreated.class);
    }

    @AfterEach
    void tearDown() {
        TransactionSynchronizationManager.setActualTransactionActive(false);
        FlowContext.clear();
    }

    @Test
    void 활성_트랜잭션_밖에서_publish하면_즉시_예외() {
        TransactionSynchronizationManager.setActualTransactionActive(false);
        FlowContext.open("t", "c", "corr", null);

        assertThatThrownBy(() -> publisher.publish(new OrderCreated("ORD-1", "1000")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("활성 트랜잭션");

        verifyNoInteractions(jdbc);   // INSERT 안 됨
    }

    @Test
    void FlowContext가_없으면_예외() {
        TransactionSynchronizationManager.setActualTransactionActive(true);
        FlowContext.clear();

        assertThatThrownBy(() -> publisher.publish(new OrderCreated("ORD-1", "1000")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("FlowContext");

        verifyNoInteractions(jdbc);
    }

    @Test
    void 정상_경로는_봉투필드와_payload를_담아_outbox에_INSERT한다() {
        TransactionSynchronizationManager.setActualTransactionActive(true);
        UUID causedBy = UUID.randomUUID();
        FlowContext.open("dongsuh", "DS-GRP", "corr-9", causedBy);

        publisher.publish(new OrderCreated("ORD-7", "1250000"));

        ArgumentCaptor<Object> args = ArgumentCaptor.forClass(Object.class);
        // INSERT INTO outbox (..10 컬럼..) VALUES (...)  — 인자 10개
        verify(jdbc).update(any(String.class), args.capture(), args.capture(), args.capture(),
                args.capture(), args.capture(), args.capture(), args.capture(),
                args.capture(), args.capture(), args.capture());

        var captured = args.getAllValues();
        // 순서: eventId, type, aggregateId, tenantId, corpId, correlationId, causedByEventId, occurredAt, version, payloadJson
        assertThat(captured.get(0)).isInstanceOf(UUID.class);
        assertThat(captured.get(1)).isEqualTo("oms.order.created");
        assertThat(captured.get(2)).isEqualTo("ORD-7");
        assertThat(captured.get(3)).isEqualTo("dongsuh");
        assertThat(captured.get(4)).isEqualTo("DS-GRP");
        assertThat(captured.get(5)).isEqualTo("corr-9");
        assertThat(captured.get(6)).isEqualTo(causedBy);
        assertThat(captured.get(8)).isEqualTo(4);   // @EventContract version
        assertThat(captured.get(9).toString()).contains("ORD-7").contains("1250000");
    }

    @Test
    void INSERT_중_오류는_직렬화_기록_실패로_감싼다() {
        TransactionSynchronizationManager.setActualTransactionActive(true);
        FlowContext.open("t", "c", "corr", null);
        org.mockito.Mockito.doThrow(new RuntimeException("db down"))
                .when(jdbc).update(any(String.class), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());

        assertThatThrownBy(() -> publisher.publish(new OrderCreated("ORD-1", "1000")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("직렬화/기록 실패");
    }

    @Test
    void 미등록_이벤트_publish는_예외() {
        TransactionSynchronizationManager.setActualTransactionActive(true);
        FlowContext.open("t", "c", "corr", null);

        // @EventContract 없는 이벤트 — EventTypes.typeOf가 미등록 예외를 던진다.
        record Unregistered(String id) implements DomainEvent {
            @Override public String aggregateId() { return id; }
        }

        assertThatThrownBy(() -> publisher.publish(new Unregistered("X")))
                .isInstanceOf(IllegalStateException.class);
    }
}
