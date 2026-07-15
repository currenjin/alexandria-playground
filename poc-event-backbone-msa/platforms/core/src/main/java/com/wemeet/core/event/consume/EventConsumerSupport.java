package com.wemeet.core.event.consume;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.Envelope;
import com.wemeet.core.event.contract.EventJson;
import com.wemeet.core.event.contract.EventTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.core.context.FlowContext;
import com.wemeet.core.inbox.InboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * 공통 소비 파이프라인. 모든 @KafkaListener가 이걸 통해 처리한다.
 * envelope 파싱 → inbox 멱등 → 컨텍스트 복원 → 핸들러 디스패치가 한 트랜잭션으로 묶여,
 * 핸들러가 실패하면 inbox 기록까지 롤백돼 재전달 때 다시 처리된다. 역직렬화 실패는 재시도 없이 DLT로 보낸다.
 */
@Component
public class EventConsumerSupport {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerSupport.class);

    private final ObjectMapper mapper = EventJson.mapper();
    private final InboxRepository inbox;
    private final HandlerRegistry registry;

    public EventConsumerSupport(InboxRepository inbox, HandlerRegistry registry) {
        this.inbox = inbox;
        this.registry = registry;
    }

    @Transactional
    public void consume(String group, String envelopeJson) {
        Envelope env;
        try {
            env = mapper.readValue(envelopeJson, Envelope.class);
        } catch (Exception e) {
            throw new NonRetryableEventException("envelope 역직렬화 실패", e);
        }

        if (!inbox.recordIfNew(group, env.eventId())) {
            log.debug("중복 skip: group={} eventId={}", group, env.eventId());
            return;
        }

        Consumer<DomainEvent> handler = registry.lookup(group, env.eventType());
        if (handler == null) {
            log.trace("핸들러 없음(무시): group={} type={}", group, env.eventType());
            return;
        }

        FlowContext.open(env.tenantId(), env.corpId(), env.correlationId(), env.eventId());
        try {
            Class<?> clazz = EventTypes.classOf(env.eventType());
            DomainEvent event = (DomainEvent) mapper.treeToValue(env.payload(), clazz);
            handler.accept(event);
        } catch (NonRetryableEventException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("핸들러 처리 실패: " + env.eventType(), e);
        } finally {
            FlowContext.clear();
        }
    }
}
