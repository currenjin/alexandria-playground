package com.currenjin.music.infrastructure.listener.song;

import com.currenjin.music.infrastructure.listener.EventListener;
import com.currenjin.music.share.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSongCreatedEventListener implements EventListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // FIXME: groupId, topics, handle messages
    @Override
    @KafkaListener(topics = "SongCreatedEvent", groupId = "song-service-group")
    public void onEvent(DomainEvent event) {
        log.info("onEvent: {}, occurredAt: {}", event.getType(), event.getOccurredAt());
    }
}
