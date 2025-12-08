package com.currenjin.music.infrastructure.listener.user;

import com.currenjin.music.infrastructure.listener.EventListener;
import com.currenjin.music.share.user.CachedUser;
import com.currenjin.music.share.user.UserCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUserCreatedEventListener implements EventListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    // FIXME: groupId, topics, handle messages
    @Override
    @KafkaListener(topics = "UserCreatedEvent", groupId = "user-service-group")
    public void onEvent(String event) {
        try {
            UserCreatedEvent createdEvent = objectMapper.readValue(event, UserCreatedEvent.class);
            log.info("onEvent: {}, occurredAt: {}", createdEvent.getType(), createdEvent.getOccurredAt());

            CachedUser cachedUser = new CachedUser(createdEvent.getId(), createdEvent.getEmail(), createdEvent.getUsername(), createdEvent.getCreatedAt());
            Cache cache = cacheManager.getCache("userCache");
            if (cache != null) { cache.put(cachedUser.id(), cachedUser); }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize DomainEvent", e);
        }
    }
}
