package com.wemeet.eventbackbone.common.event;

import com.wemeet.eventbackbone.contracts.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * (consumerGroup, eventType) → 핸들러 매핑. 도메인 서비스·사가가 자기 핸들러를 등록한다.
 * 참여자는 자기가 어느 흐름/사가에 속하는지 모른 채 이벤트/커맨드만 처리 (§7.1.5·§7.1.7).
 */
@Component
public class HandlerRegistry {

    @SuppressWarnings("rawtypes")
    private final Map<String, Consumer> handlers = new HashMap<>();

    public <T extends DomainEvent> void register(String group, Class<T> type, Consumer<T> handler) {
        EventTypes.register(type);
        handlers.put(key(group, EventTypes.typeOf(type)), handler);
    }

    @SuppressWarnings("unchecked")
    public Consumer<DomainEvent> lookup(String group, String eventType) {
        return handlers.get(key(group, eventType));
    }

    private String key(String group, String eventType) {
        return group + "|" + eventType;
    }
}
