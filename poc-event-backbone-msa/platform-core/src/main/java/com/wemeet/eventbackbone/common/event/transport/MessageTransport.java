package com.wemeet.eventbackbone.common.event.transport;

/**
 * 발행 전송 포트 — 브로커(Kafka / SQS / …) 교체 지점. 릴레이는 이 포트에만 의존한다.
 * 다른 브로커로 바꾸려면 이 인터페이스를 구현한 어댑터를 하나 추가하면 된다(릴레이·outbox·비즈 코드 무변경).
 */
public interface MessageTransport {
    void send(String topic, String key, String payload);
}
