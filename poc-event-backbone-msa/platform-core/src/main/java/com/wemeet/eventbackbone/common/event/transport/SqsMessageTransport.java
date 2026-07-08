package com.wemeet.eventbackbone.common.event.transport;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * SQS 발신 어댑터 — 스켈레톤(broker=sqs). 실제 구현은 Spring Cloud AWS
 * (io.awspring.cloud:spring-cloud-aws-starter-sqs)의 SqsTemplate 주입.
 * 브로커 교체가 "어댑터 하나 추가"임을 보여주는 자리 — 서비스·릴레이·outbox·핸들러 무변경.
 */
@Component
@ConditionalOnProperty(name = "platform.events.broker", havingValue = "sqs")
public class SqsMessageTransport implements MessageTransport {

    @Override
    public void send(String topic, String key, String payload) {
        // TODO(SQS): sqsTemplate.send(to -> to.queue(queueOf(topic)).payload(payload).messageGroupId(key));
        //            순서/리텐션/DLQ 의미는 SQS(FIFO·redrive policy)로 매핑.
        throw new UnsupportedOperationException(
                "SQS 발신 미구현(스켈레톤) — Spring Cloud AWS SqsTemplate 연결 지점. topic->큐명 매핑, FIFO면 MessageGroupId=key.");
    }
}
