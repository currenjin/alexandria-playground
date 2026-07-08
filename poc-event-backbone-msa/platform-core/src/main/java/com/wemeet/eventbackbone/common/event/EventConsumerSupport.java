package com.wemeet.eventbackbone.common.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.common.inbox.InboxRepository;
import com.wemeet.eventbackbone.contracts.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * 공통 소비 파이프라인 (§7.1.5) — 모든 @KafkaListener가 이걸 통해 처리.
 * ① 봉투 파싱 ② Inbox 멱등(ON CONFLICT DO NOTHING) ③ 컨텍스트 복원 ④ 핸들러 디스패치.
 * 전체가 한 트랜잭션: inbox 기록 + 핸들러 도메인 쓰기 + (핸들러가 발행하면) outbox INSERT가 함께 커밋.
 * 핸들러 예외 → 롤백(inbox 포함) → 재전달 시 재처리. 재시도 소진 시 DLT (§7.1.6, 컨테이너 에러 핸들러).
 */
@Component
public class EventConsumerSupport {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerSupport.class);

    private final ObjectMapper mapper;
    private final InboxRepository inbox;
    private final HandlerRegistry registry;

    public EventConsumerSupport(ObjectMapper mapper, InboxRepository inbox, HandlerRegistry registry) {
        this.mapper = mapper;
        this.inbox = inbox;
        this.registry = registry;
    }

    @Transactional
    public void consume(String group, String envelopeJson) {
        Envelope env;
        try {
            env = mapper.readValue(envelopeJson, Envelope.class);
        } catch (Exception e) {
            // 역직렬화 실패 = 재시도 무의미 → 즉시 DLT (§7.1.6). 런타임 예외로 컨테이너에 위임.
            throw new NonRetryableEventException("봉투 역직렬화 실패", e);
        }

        // ② Inbox 멱등: 확인과 기록이 한 번의 INSERT (§7.1.5) — InboxRepository로 위임
        if (!inbox.recordIfNew(group, env.eventId())) {
            log.debug("중복 skip: group={} eventId={}", group, env.eventId());
            return;                         // 이미 처리 — 핸들러 안 태움
        }

        Consumer<DomainEvent> handler = registry.lookup(group, env.eventType());
        if (handler == null) {
            log.trace("핸들러 없음(무시): group={} type={}", group, env.eventType());
            return;                         // 이 그룹이 관심 없는 타입
        }

        // ③ 컨텍스트 복원: correlationId 전파 + currentEventId=이 이벤트(다운스트림의 causation)
        FlowContext.open(env.tenantId(), env.corpId(), env.correlationId(), env.eventId());
        try {
            Class<?> clazz = EventTypes.classOf(env.eventType());
            DomainEvent event = (DomainEvent) mapper.treeToValue(env.payload(), clazz);
            handler.accept(event);          // ④ 도메인 핸들러 (같은 트랜잭션)
        } catch (NonRetryableEventException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("핸들러 처리 실패: " + env.eventType(), e);
        } finally {
            FlowContext.clear();
        }
    }
}
