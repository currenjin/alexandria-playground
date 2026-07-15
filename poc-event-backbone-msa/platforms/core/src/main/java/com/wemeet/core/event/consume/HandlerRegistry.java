package com.wemeet.core.event.consume;

import com.wemeet.core.event.contract.DomainEvent;
import com.wemeet.core.event.contract.EventTypes;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * (consumerGroup, eventType) → 핸들러 매핑. 도메인 서비스·사가가 자기 핸들러를 등록한다.
 * 참여자는 자기가 어느 흐름/사가에 속하는지 모른 채 이벤트/커맨드만 처리.
 */
@Component
public class HandlerRegistry {

    @SuppressWarnings("rawtypes")
    private final Map<String, Consumer> handlers = new HashMap<>();

    public <T extends DomainEvent> void register(String group, Class<T> type, Consumer<T> handler) {
        EventTypes.register(type);
        String key = key(group, EventTypes.typeOf(type));
        Consumer<?> existing = handlers.putIfAbsent(key, handler);
        if (existing != null) {
            // 조용히 덮어쓰면 먼저 등록된 핸들러가 무경고로 사라진다 — 기동 시점에 실패시켜 드러낸다.
            throw new IllegalStateException(
                    "중복 @EventHandler: group=%s, eventType=%s — 같은 그룹에서 같은 이벤트의 핸들러는 하나만 허용"
                            .formatted(group, EventTypes.typeOf(type)));
        }
    }

    @SuppressWarnings("unchecked")
    public Consumer<DomainEvent> lookup(String group, String eventType) {
        return handlers.get(key(group, eventType));
    }

    private String key(String group, String eventType) {
        return group + "|" + eventType;
    }
}
