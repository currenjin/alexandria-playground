package com.wemeet.core.event.transport;

import com.wemeet.core.event.consume.EventConsumerSupport;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * SQS 어댑터는 스켈레톤(broker=sqs). 발신은 미구현이라 UnsupportedOperation을 던지고,
 * 수신은 경고만 로그하는 자리표시자다 — "브로커 교체 = 어댑터 하나 추가"임을 보여주는 지점.
 */
class SqsTransportSkeletonTest {

    @Test
    void SqsMessageTransport_send는_미구현_예외를_던진다() {
        SqsMessageTransport transport = new SqsMessageTransport();

        assertThatThrownBy(() -> transport.send("topic", "key", "payload"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("SQS 발신 미구현");
    }

    @Test
    void SqsEventSubscriber는_스켈레톤이라_구독을_시도하지_않는다() {
        EventConsumerSupport support = mock(EventConsumerSupport.class);

        assertThatCode(() -> new SqsEventSubscriber(support, "saga", "oms.order,tms.dispatch"))
                .doesNotThrowAnyException();

        verifyNoInteractions(support);   // 미구현 — consume 배선 없음
    }
}
