package com.currenjin.music.infrastructure.listener.song;

import com.currenjin.music.infrastructure.listener.EventListener;
import com.currenjin.music.share.song.CachedSong;
import com.currenjin.music.share.song.SongCreatedEvent;
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
public class KafkaSongCreatedEventListener implements EventListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    // FIXME: groupId, topics, handle messages
    @Override
    @KafkaListener(topics = "SongCreatedEvent", groupId = "song-service-group")
    public void onEvent(String event) {
        try {
            SongCreatedEvent createdEvent = objectMapper.readValue(event, SongCreatedEvent.class);
            log.info("onEvent: {}, occurredAt: {}", createdEvent.getType(), createdEvent.getOccurredAt());

            CachedSong cachedSong = new CachedSong(
                    createdEvent.getId(),
                    createdEvent.getTitle(),
                    createdEvent.getArtist(),
                    createdEvent.getDurationSeconds(),
                    createdEvent.getGenre()
            );
            Cache cache = cacheManager.getCache("songCache");
            Cache existsCache = cacheManager.getCache("songExistsCache");
            if (cache != null) { cache.put(cachedSong.id(), cachedSong); }
            if (existsCache != null) { existsCache.put(cachedSong.id(), true); }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize DomainEvent", e);
        }
    }
}
