package com.currenjin.music.infrastructure.listener.song;

import com.currenjin.music.infrastructure.listener.EventListener;
import com.currenjin.music.share.DomainEvent;
import com.currenjin.music.share.song.SongCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSongCreatedEventListener implements EventListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper;

    public KafkaSongCreatedEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // FIXME: groupId, topics, handle messages
    @Override
    @KafkaListener(topics = "SongCreatedEvent", groupId = "song-service-group")
    public void onEvent(String event) {
        try {
            SongCreatedEvent createdEvent = objectMapper.readValue(event, SongCreatedEvent.class);
            log.info("onEvent: {}, occurredAt: {}", createdEvent.getType(), createdEvent.getOccurredAt());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize DomainEvent", e);
        }
    }
}
